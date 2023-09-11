<template>
  <div>
    <el-row :gutter="10" style="margin-bottom: 80px;">
      <el-col :span="6">
        <el-card style="color: #67C23A">
          <div><i class="el-icon-user" /> 用户总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            100
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #247BA0">
          <div><i class="el-icon-money" /> 销售总量</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            $ 1000000
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #FF1654">
          <div><i class="el-icon-cherry" /> 收益总额</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            $ 300000
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #028090">
          <div><i class="el-icon-house" /> 门店总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            20
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <div id="main" style="height: 400px;width: 500px;"></div>
      </el-col>
      <el-col :span="12">
        <div id="pie" style="height: 400px;width: 500px;"></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: "Home",
  data() {
    return {

    }
  },
  mounted() {
    // 折线和条形图
    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);
    var option = {
      title: {
        text: '各季度会员数量统计',
        subtext: '趋势图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      xAxis: {
        type: 'category',
        data: ["第一季度", "第二季度", "第三季度", "第四季度"]
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '星巴克',
          data: [],
          type: 'bar'
        },
        {
          name: '星巴克',
          data: [],
          type: 'line'
        },
        {
          name: '瑞幸咖啡',
          data: [],
          type: 'bar'
        },
        {
          name: '瑞幸咖啡',
          data: [],
          type: 'line'
        },
      ]
    };

    // 饼图
    var pieChartDom = document.getElementById('pie');
    var pieChart = echarts.init(pieChartDom);
    var pieOption = {
      title: {
        text: '各季度会员数量统计',
        subtext: '趋势图',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/> {b} : {c} ({d}%)'
      },
      legend: {
        orient: 'vertical',
        left: 'left'
      },
      series: [
        {
          name: '星巴克',
          type: 'pie',
          center: ['25%', '70%'],
          radius: '55%',
          // 饼图显示百分比
          label: {              // 饼图图形上的文本标签
            normal:{
              show:true,
              position:'inner', // 标签位置
              textStyle: {
                fontWeight: 300,
                fontSize: 16 // 文字字体大小
              },
              formatter: '{c} / {d}%'
            }
          },
          data: [],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        },
        {
          name: '瑞幸咖啡',
          type: 'pie',
          center: ['75%', '50%'],
          radius: '60%',
          // 饼图显示百分比
          label: {              // 饼图图形上的文本标签
            normal:{
              show:true,
              position:'inner', // 标签位置
              textStyle: {
                fontWeight: 300,
                fontSize: 16 // 文字字体大小
              },
              formatter: '{d}%'
            }
          },
          data: [ {name: '第一季度', value: 5},
                  {name: '第二季度', value: 6},
                  {name: '第三季度', value: 7},
                  {name: '第四季度', value: 8},],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    };


    // 请求数据
    this.request.get('http://localhost:9090/echarts/members').then(res => {
      // 填空
      // option.xAxis.data = res.data.x
      option.series[0].data = res.data
      option.series[1].data = res.data

      option.series[2].data = [5,6,7,8]
      option.series[3].data = [5,6,7,8]
      // 数据准备完毕之后再 set
      myChart.setOption(option)

      pieOption.series[0].data = [
        {name: '第一季度', value: res.data[0]},
        {name: '第二季度', value: res.data[1]},
        {name: '第三季度', value: res.data[2]},
        {name: '第四季度', value: res.data[3]},
      ]
      pieChart.setOption(pieOption)

    })
  }
}
</script>

<style scoped>

</style>