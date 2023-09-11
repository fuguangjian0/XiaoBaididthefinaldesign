<script>
// 信息窗体的内容
var content = [
  "<div>这是信息窗口{办公楼, 我在这里办公}</div>"
];

//获取用户所在城市信息
function showCityInfo() {
  //实例化城市查询类
  var citysearch = new AMap.CitySearch();
  //自动获取用户IP，返回当前城市
  citysearch.getLocalCity(function(status, result) {
    if (status === 'complete' && result.info === 'OK') {
      if (result && result.city && result.bounds) {
        var cityinfo = result.city;
        var citybounds = result.bounds;
        document.getElementById('info').innerHTML = '您当前所在城市：'+cityinfo;
        //地图显示当前城市
        map.setBounds(citybounds);
      }
    } else {
      document.getElementById('info').innerHTML = result.info;
    }
  });
}


export default {
  name: "Map",
  data() {
    return {

    }
  },
  created() {

  },
  mounted() {
    // 创建地图实例
    var map = new AMap.Map("container", {
      zoom: 13,
      center: [119.539702,35.455234], // 经纬度定位中心
    });

    // 创建 infoWindow 实例
    var infoWindow = new AMap.InfoWindow({
      content: content.join("<br>")  //传入 dom 对象，或者 html 字符串
    });


    var clickHandler = function(e) {
      console.log('您在[ '+e.lnglat.getLng()+','+e.lnglat.getLat()+' ]的位置点击了地图！'); // 坐标拾取打印
      // 打开信息窗体
      infoWindow.open(map, [119.539702,35.455234]);
    };


    // 绑定事件
    // map.on('click', clickHandler);


    var marker = new AMap.Marker({
      position: new AMap.LngLat(119.539702,35.455234),  // 经纬度对象, 也可以是经纬度构成的一组数组 []
      title: '济宁医学院主楼',
      icon: '//vdata.amap.com/icons/b18/1/2.png', // 添加 Icon 图标 URL
    });

    map.on('click', clickHandler);

    //将创建的点标记到已有的地图实例
    map.add(marker);


    // path 是驾车导航的起、途径和终点，最多支持16个途经点
    var path = []

    path.push([119.536183,35.45866])
    path.push([119.534981,35.452228])
    path.push([119.533608,35.440201])

    map.plugin('AMap.DragRoute', function () {
      var route = new AMap.DragRoute(map, path, AMap.DrivingPolicy.LEAST_FEE)
      // 查询导航路径并开启拖拽导航
      route.search()
    })

    // 自己加一个线路
    // var polyLine = new AMap.Polyline({
    //   path: path,
    //   strokeWeight: 5,
    //   borderWeight: 5, // 线条宽度, 默认为1
    //   strokeStyle: "dashed",
    //   strokeColor: "#428100", // 线条颜色
    //   lineJoin: 'round' // 折线拐点连接处样式
    // })
    // map.add(polyLine)

    showCityInfo();
  }

}
</script>

<template>
  <div>
    <div id="container" style="width: 100%; height: calc(100vh - 100px);">

    </div>
    <div id="info"></div>
  </div>
</template>

<style scoped>

</style>