<script>

export default {
  name: "Video",
  data() {
    return {
      videos: []
    }
  },
  methods: {
    detail(id) {
      // 路由传参
      this.$router.push({ path:'/front/videoDetail', query: {id: id} })

    }
  },
  created() {
    this.request.get("/file/front/all").then(res => {
      console.log(res.data)
      this.videos = res.data.filter(v => v.type === 'mp4')

    })
  }
}
</script>

<template>
  <div style="padding: 10px">
    <el-card>
      <div v-for="item in videos" :key="item.id" style="margin: 10px 0; padding: 10px 0; color: #666; border-bottom: 1px solid #cccccc;" >
        <span style="font-size: 18px; cursor: pointer;" class="item" @click="detail(item.id)"> {{ item.name }} </span>
        <span style="float: right; font-size: 12px;margin-top: 10px;"> 文件大小: {{ item.size }} kb</span>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.item:hover {
  color: #3F5EFB;
}
</style>

