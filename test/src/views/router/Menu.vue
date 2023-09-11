<script>
export default {
  name: "User",
  data() {
    return {
      id: '',
      name: '',
      path: '',
      icon: '',
      description: '',
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 5,
      form: {},
      multipleSelection: [],
      // 弹窗数据
      dialogFormVisible: false,
      headerBg: 'headerBg',
      options: [],
    }
  },
  methods: {
    load() {
      this.request.get(`http://localhost:9090/menu?name=${this.name}`).then(res => {
        if (res.code === '200') {
          this.tableData = res.data
        } else {
          this.$message.error('无法显示内容')
        }
      })
    },
    reset() {
      this.name = ''
      this.path = ''
      this.icon = ''
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
      this.request.post("http://localhost:9090/menu", this.form).then(res => {
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
      // 请求图标数据
      this.request.get(`http://localhost:9090/menu/icons`).then(res => {
        this.options = res.data
      })
    },
    del(id) {
      this.request.delete(`http://localhost:9090/menu/${id}`).then(res => {
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
      this.request.post('http://localhost:9090/menu/del/batch', ids).then(res => {
        if (res.code === '200') {
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
    handleAdd(pid) {
      this.dialogFormVisible = true
      this.form = {}
      if (pid) this.form.pid = pid
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

    <!--搜索框-->
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
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
    </div>

    <!--表格-->
    <!--  row-key="id" default-expand-all 渲染树形数据, 默认展开全部树  -->
    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg"
              row-key="id" default-expand-all @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="名称" width="140"></el-table-column>
      <el-table-column prop="path" label="路径" width="120"></el-table-column>
      <el-table-column prop="pagePath" label="页面路径" width="120"></el-table-column>
      <el-table-column label="图标" class-name="fontSize18" align="center" label-class-name="fontSize12">
        <template slot-scope="scope">
          <span :class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <!--两个操作表格的按钮-->
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="success"
              @click="handleAdd(scope.row.id)"
              v-if="!scope.row.pid && !scope.row.path">新增子菜单
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
              <i class="el-icon-circle-close"></i>
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>



    <!--编辑弹窗-->
    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="120px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="页面路径">
          <el-input v-model="form.pagePath" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="路径">
          <el-input v-model="form.path" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <template slot-scope="scope">
            <el-select clearable v-model="form.icon" placeholder="请选择">
              <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
                <i :class="item.value" /> {{ item.name }}
              </el-option>
            </el-select>
          </template>
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


  </div>
</template>

<style scoped>
.headerBg {
  background: #eee !important;
}

.fontSize18 {
  font-size: 18px;
}

.fontSize12 {
  font-size: 12px;
}
</style>