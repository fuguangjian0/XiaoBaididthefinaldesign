<template>
  <div style="color: #666">
    <!--  3个搜索框-->
    <div style="margin: 10px 0">
      <el-input style="width: 300px" placeholder="请输入名称" suffix-icon="el-icon-search"
                v-model="name"></el-input>
      <el-button type="primary" class="ml-5" @click="load" size="small">搜索</el-button>
      <el-button type="danger" class="ml-5" @click="reset" size="small">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <div style="padding: 20px  0; border-bottom: 1px dashed #ccc;" v-for="item in tableData">
        <div style="cursor: pointer; font-size: 20px; color: #F56C6C" @click="$router.push(`/front/articleDetail?id=${item.id}`)">{{ item.name }}</div>
        <div style="margin-top: 10px">
          <i class="el-icon-user-solid"></i><span>{{ item.user }}</span>
          <i style="margin-left: 5px" class="el-icon-time"></i><span>{{ item.time }}</span>
        </div>
      </div>
    </div>


    <!--分页插件-->
    <div class="pd-10">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, prev, pager, next"
          :total="total">
      </el-pagination>
    </div>



    <el-dialog title="文章信息" :visible.sync="dialogViewVisible" width="60%">
      <el-card>
        <!--文章预览-->
        <div>
          <mavon-editor
            class="md"
            :value="content"
            :subfield="false"
            :defaultOpen="'preview'"
            :tollbarsFlag="false"
            :editable="false"
            :scroll-style="true"
            :ishljs="true" />
        </div>
      </el-card>
    </el-dialog>



  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "File",
  data() {
    return {
      name: '',
      teachers: [],
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      form: {},
      multipleSelection: [],
      // 弹窗数据
      dialogFormVisible: false,
      headerBg: 'headerBg',
      user: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {},
      content: '',
      dialogViewVisible: false
    }
  },
  created() {
    // 请求分页查询数据
    this.load()
  },
  methods: {
    view(content) {
      this.content = content
      this.dialogViewVisible = true
    },
    // 绑定@imgAdd event
    imgAdd(pos, $file) {
      let $vm = this.$refs.md
      // 第一步,将图片上传到服务器
      const formData = new FormData()
      formData.append('file', $file)
      axios({
        url: 'http://localhost:9090/file/upload',
        method: 'post',
        data: formData,
        headers: {'Content-Type': 'multipart/form-data'},
      }).then(res => {
        // 第二步, 将返回的url替换到文本原位置![...](./0) -> ![...](url)
        $vm.$img2Url(pos, res.data)
      })
    },
    load() {
      this.request.get(`/article/page?pageNum=${this.pageNum}&pageSize=${this.pageSize}&name=${this.name}`).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.records
          console.log(this.tableData);
          this.total = res.data.total
        } else {
          this.$message.error('无法显示内容')
        }
      })
      // 请求
      this.request.get(`/user/role/ROLE_TEACHER`).then(res => {
              this.teachers = res.data
      })
    },
    addRecords() {
      this.dialogFormVisible = true
    },
    reset() {
      this.name = ''
      this.load()
    },
    batchDeletion() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1, 2, 3]
      this.request.post('/article/del/batch', ids).then(res => {
        if (res.code === '200') {
          this.$message.success('批量删除成功')
          this.load()
        } else {
          this.$message.error('批量删除失败')
        }
      })
    },
    save() {
      this.request.post("/article", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success('新增成功')
          this.form = {}
          this.load()
          this.dialogFormVisible = false
        } else {
          this.$message.error('新增失败')
        }
      })
    },
    download(url) {
      window.open(url)
    },
    del(id) {
      this.request.delete(`/article/${id}`).then(res => {
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
      this.request.post("/article/update", row).then(res => {
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
    edit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
  }
}
</script>

<style scoped>
.headerBg {
  background: #eee !important;
}
</style>



