package com.fgj.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgj.common.Constants;
import com.fgj.common.Result;
import com.fgj.entity.Files;
import com.fgj.exception.ServiceException;
import com.fgj.mapper.FileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {


    @Value("${file.upload.path}")
    String uploadPath;

    @Resource
    private FileMapper fileMapper;



    @GetMapping("/front/all")
    public Result getFrontMes() {
        return Result.success(fileMapper.selectList(null));
    }



    /**
     * 文件上传接口
     * @param file 前端传过来的文件流
     * @return 文件id
     * @throws Exception 文件 保存失败抛出
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        // 定义一个文件唯一的标识码
        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;

        File uploadFile = new File(uploadPath + fileUUID);
        // 判断配置文件的保存路径是否存在, 若不存在则创建一个文件路径
        if (!uploadFile.getParentFile().exists()) {
            uploadFile.getParentFile().mkdirs();
        }

        String url;
        // 获取文件的md5
        String md5 = SecureUtil.md5(file.getInputStream());
        // 从数据库查询是否存在相同的记录
        Files dbFiles = getFileByMd5(md5);
        // 如果存在相同的md5 则抛出异常
        if (dbFiles != null) {
            url = dbFiles.getUrl();
        } else {
            // 上传文件到磁盘
            file.transferTo(uploadFile);
            // 数据库不存在重复文件, 则不删除对应的文件
            url = "http://localhost:9090/file/" + fileUUID;
        }

        // 存储数据库
        Files saveFile = new Files();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size/1024);
        saveFile.setUrl(url);
        saveFile.setMd5(md5);
        fileMapper.insert(saveFile);

        return url;

    }

    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // 根据uuid 下载文件
        File uploadFile = new File(uploadPath + fileUUID);
        // 设置输出流格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename= "+ URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");
        
        // 读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }

    @GetMapping("/page")
    public Result page(@RequestParam Integer pageNum, @RequestParam Integer pageSize, @RequestParam String filename) {
        LambdaQueryWrapper<Files> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Files::getId);
        queryWrapper.eq(Files::getIsDelete, false);
        queryWrapper.like(Files::getName, filename);
        Page<Files> page = new Page<>(pageNum, pageSize);
        Page<Files> all = fileMapper.selectPage(page, queryWrapper);
        if (all != null) {
            return Result.success(all);
        } else {
            throw new ServiceException(Constants.CODE_500, "分页查询失败");
        }
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        Files files = fileMapper.selectById(id);
        files.setIsDelete(true);
        fileMapper.updateById(files);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result delBatch(@RequestBody List<Integer> ids) {
        for (Integer id : ids) {
            Files files = fileMapper.selectById(id);
            files.setIsDelete(true);
            fileMapper.updateById(files);
        }
        return Result.success();
    }


    private Files getFileByMd5(String md5) {
        // 查询文件的md5是否存在
        List<Files> filesList = fileMapper.selectList(new QueryWrapper<Files>().eq("md5", md5));
        log.info("查询结果的数量：{}", filesList.size());
        return filesList.size() == 0 ? null : filesList.get(0);
    }

    @PostMapping("/update")
    public Result update(@RequestBody Files files) {
        fileMapper.updateById(files);
        return Result.success();
    }

    // 根据id查询记录
    @GetMapping("/detail/{id}")
    public Result getById(@PathVariable String id) {

        return null;
    }


}

