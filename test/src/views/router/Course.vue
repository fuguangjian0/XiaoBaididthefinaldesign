<template>
  <div>
    <!--  3个搜索框-->
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search"
                v-model="name"></el-input>
      <el-button type="primary" class="ml-5" @click="load">搜索</el-button>
      <el-button type="danger" class="ml-5" @click="reset">重置</el-button>
    </div>

    <!--上传文件 批量删除-->
    <div style="margin: 10px 0">
      <el-button type="success" @click="addRecords" v-if="user.role === 'ROLE_ADMIN'">新增 <i class="el-icon-plus"/></el-button>

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
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"
              @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"/>
      <el-table-column prop="id" label="ID" width="80"/>
      <el-table-column prop="name" label="课程名称" width="140"/>
      <el-table-column prop="score" label="学分" width="120"/>
      <el-table-column prop="times" label="课时"/>
      <el-table-column prop="teacherId" label="授课教师ID"/>
      <el-table-column prop="teacher" label="授课老师"/>
      <el-table-column label="启用">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc"
                     @change="changeEnable(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="300">
        <template slot-scope="scope">
          <el-button type="primary" @click="selectCourse(scope.row.id)">选课</el-button>
          <el-button
              size="mini"
              type="primary"
              @click="edit(scope.row)"
              v-if="user.role === 'ROLE_ADMIN'">编辑
            <i class="el-icon-edit"></i>
          </el-button>
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

    <!--弹窗-->
    <el-dialog title="课程信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="120px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="学分">
          <el-input v-model="form.score" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="课时">
          <el-input v-model="form.times" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="教师">
          <el-select clearable v-model="form.teacherId" placeholder="请选择教师" style="width: 100%">
            <el-option v-for="item in teachers" :key="item.id" :label="item.nickname" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
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
      user: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {}
    }
  },
  created() {
    // 请求分页查询数据
    this.load()
  },
  methods: {
    selectCourse(courseId) {
      this.request.post(`/course/studentCourse/${courseId}/${this.user.id}`).then(res => {
        if (res.code === '200') {
          this.$message.success('选课成功')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    load() {
      this.request.get(`/course/page?pageNum=${this.pageNum}&pageSize=${this.pageSize}&name=${this.name}`).then(res => {
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
      this.request.post('/course/del/batch', ids).then(res => {
        if (res.code === '200') {
          this.$message.success('批量删除成功')
          this.load()
        } else {
          this.$message.error('批量删除失败')
        }
      })
    },
    save() {
      this.request.post("/course", this.form).then(res => {
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
      this.request.delete(`/course/${id}`).then(res => {
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
      this.request.post("/course/update", row).then(res => {
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



