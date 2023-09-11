<template>
  <div style="color: #666">

    <!--文章信息: 标题 作者 时间-->
    <div>
      <div style="cursor: pointer; font-size: 20px; color: #F56C6C">{{ article.name }}</div>
      <div style="margin-top: 10px">
        <i class="el-icon-user-solid"></i><span>{{ article.user }}</span>
        <i style="margin-left: 5px" class="el-icon-time"></i><span>{{ article.time }}</span>
      </div>
    </div>

    <!--文章内容-->
    <div style="margin: 20px 0">
      <mavon-editor
          class="md"
          :value="article.content"
          :subfield="false"
          :defaultOpen="'preview'"
          :tollbarsFlag="false"
          :editable="false"
          :scroll-style="true"
          :ishljs="true" />
    </div>

    <!--评论框-->
    <div style="margin: 50px 0">
      <div style="margin: 10px 0">
        <div style="border-bottom: 1px solid orangered; padding: 10px 0; font-size: 20px;">评论</div>
        <div style="padding: 10px 0">
          <el-input  size="small" type="textarea" v-model="commentForm.content"></el-input>
        </div>
        <div class="pd-10" style="text-align: right">
          <el-button type="primary" size="small" @click="save">评论</el-button>
        </div>
      </div>
    </div>

    <!--评论列表-->
    <div>
      <div v-for="item in comments" :key="item.id" style="border-bottom: 1px solid #cccccc; padding: 10px 0; display: flex">
        <div style="display: flex">
          <!--头像-->
          <div style="width: 100px; text-align: center; ">
            <el-image :src="item.avatarUrl" style="width: 50px; height: 50px; border-radius: 50%;"></el-image>
          </div>
          <!--内容-->
          <div style="flex: 1; font-size: 14px; padding: 5px 0; ">
            <b>{{ item.nickname }}:</b>
            <span>{{ item.content }}</span>
            <div style="text-align: right; display: flex">
              <div>
                <i class="el-icon-time" /><span>{{ item.time }}</span>
              </div>
              <div style="text-align:right; flex: 1;">
                <el-button style="margin-left: 5px" type="text" @click="handleReply(item.id)">回复</el-button>
                <el-button style="color: red" type="text" @click="del(item.id)" v-if="user.id === item.userId">删除</el-button>
              </div>
            </div>
          </div>
        </div>

        <!--回复列表-->
        <div v-if="item.children.length" style="padding-left: 50px; ">
          <div v-for="subItem in item.children" :key="subItem.id" style="background-color: #f0f0f0;">

            <div style=" font-size: 14px; padding: 5px 0; ">
              <div>
                <b v-if="subItem.pnickname">@{{ subItem.pnickname }}:</b>
              </div>
              <div>
                <b>{{ subItem.nickname }}:</b>
                <span>{{ subItem.content }}</span>
              </div>

              <div style="text-align: right; display: flex; line-height: 20px; ">
                <div style="width: 200px;">
                  <i class="el-icon-time" /><span style="margin-left: 5px;">{{ subItem.time }}</span>
                </div>
                <div style="flex: 1; text-align: right;">
                  <el-button style="margin-left: 5px" type="text" @click="handleReply(subItem.id)">回复</el-button>
                  <el-button style="color: red" type="text" @click="del(subItem.id)" v-if="user.id === subItem.userId">删除</el-button>
                </div>
              </div>
            </div>
          </div>
        </div>


      </div>
    </div>


    <!--弹窗-->
    <el-dialog title="文章信息" :visible.sync="dialogFormVisible" width="60%">
      <el-form label-width="120px" size="small">
        <el-form-item label="回复内容">
          <el-input type="textarea" v-model="commentForm.contentReply" autocomplete="off"></el-input>
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
      article: {},
      comments: [],
      commentForm: {},
      dialogFormVisible: false,
      id: this.$route.query.id,
      user: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {},
    }
  },
  created() {
    // 请求分页查询数据
    this.load()
    this.loadComment()
  },
  methods: {
    load() {
      this.request.get(`/article/${this.id}`).then(res => {
          this.article = res.data
      })
    },
    loadComment() {
      this.request.get(`/comment/tree/${this.id}`).then(res => {
        console.log(this.id);
        console.log(res.data);
        this.comments = res.data
      })
    },
    save() {
      if (!this.user.id) {
        this.$message.warning('请登录后操作')
      }
      this.commentForm.articleId = this.id
      if (this.commentForm.contentReply) {
        this.commentForm.content = this.commentForm.contentReply
      }
      this.request.post("/comment", this.commentForm).then(res => {
        if (res.code === '200') {
          this.$message.success('评论成功')
          this.commentForm = {} // 初始化评论对象内容
          this.loadComment()
          this.dialogFormVisible = false
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    del(id) {
      this.request.delete(`/comment/${id}`).then(res => {
        console.log(res);
        if (res.code === '200') {
          this.$message.success('删除成功')
          this.loadComment()
        } else {
          this.$message.error('删除失败')
        }
      })
    },
    // 回复
    handleReply(pid) {
      this.commentForm = { pid: pid } // 数据置空
      this.dialogFormVisible = true // 打开弹窗

    }
  }
}
</script>

<style scoped>
.headerBg {
  background: #eee !important;
}
</style>



