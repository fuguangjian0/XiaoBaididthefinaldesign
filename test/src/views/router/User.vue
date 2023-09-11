<script>
import role from "@/views/router/Role.vue";

export default {
  name: "User",
  computed: {
    role() {
      return role
    }
  },
  data() {
    return {
      username: '',
      email: '',
      address: '',
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      form: {},
      multipleSelection: [],
      // 弹窗数据
      dialogFormVisible: false,
      headerBg: 'headerBg',
      roles: [],
      courses: [],
      vis: false,
      stuVis: false,
      stuCourses: [],
    }
  },
  methods: {
    lookCourse(courses) {
      this.courses = courses
      this.vis = true
    },
    lookStuCourse(stuCourses) {
      this.stuCourses = stuCourses
      this.stuVis = true
    },
    handleExcelImportSuccess() {
      this.$message.success('导入成功')
      this.load()
    },
    load() {
      this.request.get(`/user/page?pageNum=${this.pageNum}&pageSize=${this.pageSize}
        &username=${this.username}&email=${this.email}&address=${this.address}`).then(res => {
          if (res.code === '200') {
            console.log(res.data.records);
            this.tableData = res.data.records
            this.total = res.data.total
          } else {
            this.$message.error('无法显示内容')
          }
      })
      this.request.get(`/role`).then(res => {
        this.roles = res.data
      })
    },
    reset() {
      this.username = ''
      this.email = ''
      this.address = ''
      this.load()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    addRecords() {
      this.dialogFormVisible = true
    },
    save() {
      this.request.post("/user", this.form).then(res => {
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
    edit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete(`/user/${id}`).then(res => {
        if (res.code === '200') {
          this.$message.success('删除成功')
          this.load()
        } else {
          this.$message.error('删除失败')
        }
      })
    },
    batchDeletion() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1, 2, 3]
      this.request.post('/user/del/batch', ids).then(res => {
        if(res.code === '200') {
          this.$message.success('批量删除成功')
          this.load()
        } else {
          this.$message.error('批量删除失败')
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val);
      this.multipleSelection = val;
    },
    exp() {
      window.open('/user/export')
    }
  },
  created() {
    // 请求分页查询数据
    this.load()
  }
}
</script>

<template>
<div>

  <!--  3个搜索框-->
  <div style="margin: 10px 0">
    <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="username"></el-input>
    <el-input style="width: 200px" class="ml-5" placeholder="请输入邮箱" suffix-icon="el-icon-message" v-model="email"></el-input>
    <el-input style="width: 200px" class="ml-5" placeholder="请输入地址" suffix-icon="el-icon-position" v-model="address"></el-input>
    <el-button type="primary" class="ml-5" @click="load">搜索</el-button>
    <el-button type="danger" class="ml-5" @click="reset">重置</el-button>
  </div>

  <!--新增 删除 导入 导出-->
  <div style="margin: 10px 0">
    <el-button type="success" @click="addRecords">新增 <i class="el-icon-plus"/></el-button>
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
    <el-upload
        class="upload-demo"
        action="/user/import" style="display: inline-block" list-type="xlsx" :show-file-list="false" :on-success="handleExcelImportSuccess">
      <el-button class="ml-5" type="primary">导入 <i class="el-icon-download"/></el-button>
    </el-upload>
    <el-button type="primary" @click="exp" class="ml-5">导出 <i class="el-icon-upload2"/></el-button>
  </div>

  <!--表格-->
  <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="id" label="ID" width="80"></el-table-column>
    <el-table-column prop="username" label="用户名" width="140"></el-table-column>
    <el-table-column prop="role" label="角色" width="140">
      <template slot-scope="scope">
        <el-tag type="primary" v-if="scope.row.role === 'ROLE_ADMIN'">管理员</el-tag>
        <el-tag type="warning" v-if="scope.row.role === 'ROLE_TEACHER'">教师</el-tag>
        <el-tag type="success" v-if="scope.row.role === 'ROLE_STUDENT'">学生</el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>
    <el-table-column prop="email" label="邮箱"></el-table-column>
    <el-table-column prop="phone" label="电话"></el-table-column>
    <el-table-column prop="address" label="地址"></el-table-column>
    <!--两个操作表格的按钮-->
    <el-table-column label="操作" width="300">
      <template slot-scope="scope">
        <el-button size="mini" type="primary" @click="lookCourse(scope.row.courses)" v-if="scope.row.role === 'ROLE_TEACHER'">
          查看教授课程<i class="el-icon-apple"></i>
        </el-button>
        <el-button size="mini" type="success" @click="lookStuCourse(scope.row.stuCourses)" v-if="scope.row.role === 'ROLE_STUDENT'">
          查看已选课程<i class="el-icon-apple"></i>
        </el-button>
        <el-button size="mini" type="success" @click="edit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
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
  <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
    <el-form label-width="120px" size="small">
      <el-form-item label="用户名">
        <el-input v-model="form.username" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="角色">
        <el-select clearable v-model="form.role" placeholder="请选择角色" style="width: 100%">
          <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.flag"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="form.nickname" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
  <el-dialog title="课程信息" :visible.sync="vis" width="30%">
    <el-table :data="courses" border stripe>
      <el-table-column prop="name" label="课程名称"></el-table-column>
      <el-table-column prop="score" label="学分"></el-table-column>
    </el-table>
  </el-dialog>
  <el-dialog title="课程信息" :visible.sync="stuVis" width="30%">
    <el-table :data="courses" border stripe>
      <el-table-column prop="name" label="课程名称"></el-table-column>
      <el-table-column prop="score" label="学分"></el-table-column>
    </el-table>
  </el-dialog>
</div>
</template>

<style scoped>
.headerBg {
  background: #eee !important;
}
</style>