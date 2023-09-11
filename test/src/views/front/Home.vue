<template>
  <div>
    <div style="margin: 10px 0;">
      <el-carousel :interval="4000" type="card" height="300px">
        <el-carousel-item v-for="item in imgs" :key="item">
          <img :src="item" alt="">
        </el-carousel-item>
      </el-carousel>
    </div>
    <div style="margin: 10px 0">
      <el-row :gutter="10">
        <el-col :span="4" v-for="item in files" :key="item.id" style="margin-bottom: 10px;">
          <img :src="item.url" alt="" style="width: 100%;">
          <div style="padding: 10px 0; border: 1px solid #eeeeee">
            <span style="color: #666">{{ item.name }}</span>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: "FrontHome",
  data() {
    return {
      imgs: [
          'https://cdn.cloudflare.steamstatic.com/steam/apps/1716740/ss_68f15d580bf91971f637be5e464bc803482d78f7.600x338.jpg',
          'https://cdn.cloudflare.steamstatic.com/steam/apps/1086940/ss_387896248b42a15239a9256844b3e4bff72fbc2a.600x338.jpg',
          'https://cdn.cloudflare.steamstatic.com/steam/apps/1172470/ss_52d1b774ee7e451825448a12fd3227dd196d6c1f.600x338.jpg',
          'https://cdn.cloudflare.steamstatic.com/steam/apps/2338770/ss_8c214bbc6c34b2d681628e94c2f3158cd9d6eb3a.600x338.jpg',
          'https://cdn.cloudflare.steamstatic.com/steam/apps/1938090/ss_fe25f7cce00d0a1e0824269eb338ab05e154bc12.600x338.jpg',
      ],
      files: []
    }
  },
  created() {
    this.request.get('/front').then(res => {
      this.files = res.data
      console.log(res.data);
    })
  }
}
</script>

<style scoped>
.el-carousel__item h3 {
  color: #475669;
  font-size: 14px;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}
</style>