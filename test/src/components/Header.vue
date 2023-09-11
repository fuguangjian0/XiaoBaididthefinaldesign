<script>

export default {
  name: "Header",
  props: {
    collapseBtnClass: String,
    collapse: Function,
    user: Object,
  },
  data() {
    return {
      imgUrl: 'https://x.imgs.ovh/x/2023/09/03/64f4a2f67e942.jpg',
    }
  },
  computed: {
    currentPathName () {
      return this.$store.state.currentPathName;　　//需要监听的数据
    },
    defaultImgUrl: function() {
      return this.imgUrl;
    }
  },
  watch: {
    currentPathName (newVal, oldVal) {
      console.log(newVal, oldVal)
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

<template>
  <div style="line-height: 60px; display: flex">
    <div style="flex: 1; font-size: 18px">
      <!--折叠导航栏按钮-->
      <span :class="collapseBtnClass" style="cursor: pointer" @click="collapse"></span>

      <!--添加页签-->
      <el-breadcrumb separator="/" style="display: inline-block; margin-left: 10px">
        <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{ currentPathName }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <el-dropdown style="width: 100px; cursor: pointer ">
      <div style="display: inline-block">
        <img src="" alt="">
        <img :src="user.avatarUrl ? user.avatarUrl : defaultImgUrl" alt="" style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px;">
        <span>{{ user.nickname ? user.nickname : '用户' }}</span><i class="el-icon-arrow-down" style="margin-left: 5px;"></i>
      </div>
      <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
        <router-link style="font-size: 14px; padding: 5px 0" to="/person">个人信息</router-link>
        </el-dropdown-item>
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
        <router-link style="font-size: 14px; padding: 5px 0" to="/password">修改密码</router-link>
        </el-dropdown-item>
        <el-dropdown-item style="font-size: 14px; padding: 5px 0">
          <span style="text-decoration: none" @click="logout">退出</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<style scoped>

</style>