<template>
  <div class="home">
    <el-container style="min-height: 100vh;">
      <!--左边侧边栏-->
      <el-aside :width="sideWidth + 'px'"
                style="background-color: rgb(238, 241, 246); box-shadow: 2px 0 6px rgb(0 21 41 / 35%)">
        <Aside :logoTextShow="logoTextShow" :isCollapse="isCollapse"/>
      </el-aside>

      <el-container>
        <!--header 头部-->
        <el-header style=" border-bottom: 1px solid #ccc; ">
          <!-- 此处是伸缩导航栏图标 -->
          <Header :collapse="collapse" :collapseBtnClass="collapseBtnClass" :user="user"/>
        </el-header>

        <!-- main 主体 -->
        <el-main>
          <router-view @refreshUser="getUser"/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import Aside from "@/components/Aside.vue";
import Header from "@/components/Header.vue";

export default {
  name: 'Manage',
  components: {
    Header,
    Aside,
  },
  created() {
    // 从后台获取最新的数据
    this.getUser()
  },
  data() {
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      logoTextShow: true,
      user: {},
    }
  },
  methods: {
    // 折叠栏
    collapse() {
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {
        this.sideWidth = 60
        this.collapseBtnClass = 'el-icon-s-unfold'
        this.logoTextShow = false
      } else {
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
        this.logoTextShow = true
      }
    },
    // 更新浏览器存储的用户信息
    getUser() {
      let username = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).username : ''
      // 从后台获取User的数据
      this.request.get(`http://localhost:9090/user/username/${username}`).then(res => {
        this.user = res.data // 重新赋值最新User数据
      })
    }
  }
}


</script>


