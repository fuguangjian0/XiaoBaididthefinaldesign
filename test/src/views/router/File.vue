<script>
export default {
  name: "File",
  data() {
    return {
      filename: '',
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      form: {},
      multipleSelection: [],
      // 弹窗数据
      dialogFormVisible: false,
      headerBg: 'headerBg',
    }
  },
  created() {
    // 请求分页查询数据
    this.load()
  },
  methods: {
    load() {
      this.request.get(`http://localhost:9090/file/page?pageNum=${this.pageNum}&pageSize=${this.pageSize}&filename=${this.filename}`).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.records
          this.total = res.data.total
        } else {
          this.$message.error('无法显示内容')
        }
      })
    },
    reset() {
      this.filename = ''
      this.load()
    },
    handleFileUploadSuccess(res) {
      console.log(res)
      this.$message.success("上传成功")
      this.load()
    },
    batchDeletion() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1, 2, 3]
      this.request.post('http://localhost:9090/file/del/batch', ids).then(res => {
        if(res.code === '200') {
          this.$message.success('批量删除成功')
          this.load()
        } else {
          this.$message.error('批量删除失败')
        }
      })
    },
    download(url) {
      window.open(url)
    },
    del(id) {
      this.request.delete(`http://localhost:9090/file/${id}`).then(res => {
        console.log(res);
        if (res.code === '200') {
          this.$message.success('删除成功')
          this.load()
        } else {
          this.$message.error('删除失败')
        }
      })
    },
    changeEnable(row) {
      this.request.post("http://localhost:9090/file/update", row).then(res => {
        if (res.code === '200') {
          this.$message.success("操作成功")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val);
      this.multipleSelection = val;
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
  }
}
</script>

<template>
  <div>
    <!--  3个搜索框-->
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入文件名称" suffix-icon="el-icon-search" v-model="filename"></el-input>
      <el-button type="primary" class="ml-5" @click="load">搜索</el-button>
      <el-button type="danger" class="ml-5" @click="reset">重置</el-button>
    </div>

    <!--上传文件 批量删除-->
    <div style="margin: 10px 0">
      <el-upload :action="'http://localhost:9090/file/upload'" :show-file-list="false"
                 :on-success="handleFileUploadSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">上传文件 <i class="el-icon-top"></i></el-button>
      </el-upload>

      <el-popconfirm
          class="ml-5"
          confirm-button-text='好的'
          cancel-button-text='不用了'
          icon="el-icon-minus"
          icon-color="red"
          title="这是一段内容确定删除吗？"
          @confirm="batchDeletion"
      >
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-minus"/></el-button>
      </el-popconfirm>
    </div>

    <!--表格-->
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="文件名称" width="140"></el-table-column>
      <el-table-column prop="type" label="文件类型" width="120"></el-table-column>
      <el-table-column prop="size" label="文件大小(kb)"></el-table-column>
      <el-table-column label="下载">
        <template slot-scope="scope">
          <el-button type="primary" @click="download(scope.row.url)">下载<i class="el-icon-download"></i></el-button>
        </template>
      </el-table-column>
      <el-table-column label="启用">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc"
                     @change="changeEnable(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-popconfirm
              class="ml-5"
              confirm-button-text='好的'
              cancel-button-text='不用了'
              icon="el-icon-info"
              icon-color="red"
              title="这是一段内容确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button
                size="mini"
                type="danger" slot="reference">删除
              <i class="el-icon-circle-close"></i>
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!--分页插件-->
    <div class="pd-10">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

  </div>
</template>

<style scoped>
.headerBg {
  background: #eee !important;
}
</style>



