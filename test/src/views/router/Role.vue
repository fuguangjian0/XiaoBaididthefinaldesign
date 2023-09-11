

<template>
<div>

  <!--搜索框-->
  <div style="margin: 10px 0">
    <el-input style="width: 200px" placeholder="请输入角色" suffix-icon="el-icon-search" v-model="name"></el-input>
    <el-button type="primary" class="ml-5" @click="load">搜索</el-button>
    <el-button type="danger" class="ml-5" @click="reset">重置</el-button>
  </div>

  <!--新增&批量删除-->
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
      <el-button type="danger" slot="reference"> 批量删除 <i class="el-icon-minus"/></el-button>
    </el-popconfirm>
  </div>

  <!--表格-->
  <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="120"></el-table-column>
    <el-table-column prop="id" label="ID" width="120"></el-table-column>
    <el-table-column prop="name" label="角色" width="300"></el-table-column>
    <el-table-column prop="flag" label="唯一标识" width="300"></el-table-column>
    <el-table-column prop="description" label="描述" width="300"></el-table-column>
    <!--两个操作表格的按钮-->
    <el-table-column label="操作">
      <template slot-scope="scope">
        <el-button
            size="mini"
            @click="assignMenu(scope.row)">分配菜单
          <i class="el-icon-menu"></i>
        </el-button>
        <el-button
            size="mini"
            type="primary"
            @click="edit(scope.row)">编辑
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
            <i class="el-icon-circle-close" />
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

  <!--编辑弹窗-->
  <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
    <el-form label-width="120px" size="small">
      <el-form-item label="角色">
        <el-input v-model="form.name" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="唯一标识">
        <el-input v-model="form.flag" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="描述">
        <el-input v-model="form.description" autocomplete="off"></el-input>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFormVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>

  <!--分配弹窗-->
  <el-dialog title="分配菜单" :visible.sync="dialogMenuVisible" width="30%">
    <el-tree
        node-key="id"
        :data="menuData"
        show-checkbox
        ref="tree"
        :default-expanded-keys="expands"
        :default-checked-keys="checks"
        :props="props">
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span><i :class="data.icon" /> {{ data.name }}</span>
      </span>
    </el-tree>
    <div slot="footer" class="dialog-footer">
      <el-button @click="dialogMenuVisible = false">取 消</el-button>
      <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
    </div>
  </el-dialog>


</div>
</template>

<script>
export default {
  name: "User",
  data() {
    return {
      menuData: [],
      id: '',
      name: '',
      description: '',
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      roleId: 0,
      roleFlag: '',
      form: {},
      multipleSelection: [],
      // 弹窗数据
      dialogFormVisible: false,
      dialogMenuVisible: false,
      headerBg: 'headerBg',
      props: {
        label: 'name',
      },
      checks: [],
      expands: [],
    }
  },
  methods: {
    load() {
      this.request.get(`http://localhost:9090/role/page?pageNum=${this.pageNum}
      &pageSize=${this.pageSize}&name=${this.name}`).then(res => {
        if (res.code === '200') {
          this.tableData = res.data.records
          this.total = res.data.total
        } else {
          this.$message.error('无法显示内容')
        }
      })
    },
    reset() {
      this.name = ''
      this.description = ''
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
      this.request.post("http://localhost:9090/role", this.form).then(res => {
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
    saveRoleMenu() {
      this.request.post(`http://localhost:9090/role/roleMenu/${this.roleId}`, this.$refs.tree.getCheckedKeys()).then(res => {
        if (res === '200') {
          this.$message.success('分配成功')
          this.dialogMenuVisible = false
          if (this.roleFlag === 'ROLE_ADMIN')
            this.$store.commit('logout')
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    edit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete(`http://localhost:9090/role/${id}`).then(res => {
        if (res.code === '200') {
          this.$message.success('删除成功')
          this.load()
        } else {
          this.$message.error('删除失败')
        }
      })
    },
    assignMenu(role) {
      this.dialogMenuVisible = true
      this.roleId = role.id
      this.roleFlag = role.flag
      // 请求菜单数据
      this.request.get(`http://localhost:9090/menu`).then(res => {
        this.menuData = res.data
        // 后台返回的菜单数据处理成 id 数组
        this.expands = this.menuData.map(v => v.id)
      })
      // 默认选中
      this.request.get(`http://localhost:9090/role/roleMenu/${this.roleId}`).then(res => {
        this.checks = res.data

        this.request.get('http://localhost:9090/menu/ids').then(r => {
          const ids = r.data
          ids.forEach(id => {
            if (!this.checks.includes(id)) {
              this.$refs.tree.setChecked(id, false, false)
            }
          })
        })

      })
    },
    batchDeletion() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1, 2, 3]
      this.request.post('http://localhost:9090/role/del/batch', ids).then(res => {
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
    }
  },
  created() {
    // 请求分页查询数据
    this.load()
  }
}
</script>

<style scoped>
.headerBg {
  background: #eee !important;
}
</style>