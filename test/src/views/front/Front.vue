<template>
  <div>
    <!--  头部-->
    <div style="display: flex; height: 60px; line-height: 60px; border-bottom: 1px solid #eee;">

      <!--      左边固定,右边固定,中间自适应-->
      <div style="width: 300px; display: flex; padding-left: 30px;">
        <div style="width: 60px;">
          <img src="@/assets/fLogo.png" alt="" style="width: 50px; position: relative; top: 5px; right: 0">
        </div>

        <div style="flex: 1">
          欢迎来到教务系统
        </div>

      </div>

      <div style="flex: 1">
        <el-menu :default-active="1" class="el-menu-demo" mode="horizontal" router>
          <el-menu-item index="/front/home">首页</el-menu-item>
          <el-menu-item index="/front/video">视频播放</el-menu-item>
          <el-menu-item index="/front/article">文章列表</el-menu-item>
          <el-submenu index="2">
            <template slot="title">我的工作台</template>
            <el-menu-item index="2-1">选项1</el-menu-item>
            <el-menu-item index="2-2">选项2</el-menu-item>
            <el-menu-item index="2-3">选项3</el-menu-item>
            <el-submenu index="2-4">
              <template slot="title">选项4</template>
              <el-menu-item index="2-4-1">选项1</el-menu-item>
              <el-menu-item index="2-4-2">选项2</el-menu-item>
              <el-menu-item index="2-4-3">选项3</el-menu-item>
            </el-submenu>
          </el-submenu>
          <el-menu-item index="3" disabled>消息中心</el-menu-item>
          <el-menu-item index="4"><a href="https://www.ele.me" style="text-decoration: none"
                                     target="_blank">订单管理</a></el-menu-item>
        </el-menu>
      </div>
      <div style="width: 200px">
        <div v-if="!user.username" style="text-align: right; padding-right: 30px;">
          <el-button type="success" @click="$router.push('/login')">登录</el-button>
          <el-button type="primary" @click="$router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown style="width: 100px; cursor: pointer ">
            <div style="display: inline-block">
              <img src="" alt="">
              <img :src="user.avatarUrl" alt=""
                   style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px;">
              <span>{{ user.nickname }}</span><i class="el-icon-arrow-down" style="margin-left: 5px;"></i>
            </div>
            <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
              <router-link style="font-size: 14px; padding: 5px 0" to="/person">个人信息</router-link>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <span style="text-decoration: none" @click="logout">退出</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>
    </div>

    <!--    主页居中显示-->
    <div style="width: 1200px; margin: 0 auto">
      <router-view/>
    </div>

  </div>
</template>

<script>
export default {
  name: "Front",
  data() {
    return {
      user: localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {}
    }
  },
  methods: {
    logout() {
      this.$store.commit('logout')
      this.$message.success('退出成功')
    }
  }
}


</script>

<style scoped>

</style>
