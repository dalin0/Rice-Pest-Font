<template>
  <div class="block wow slideInLeft">
	<h1 class="title">历史记录</h1>
	<ul>
		<li :key="index" v-for="(item,index) in historyList">
			<el-row type="flex" gutter="10" justify="center" style="margin: 10px;">
				<el-col :span="6">
					<el-image 
					style = "width: 100%;height: 150px;"
					v-bind:src = item.pic ></el-image>
				</el-col>
				<el-col :span="18">
					<div class="describe">	
						<p><em class="reg_res w3">检测结果：</em><em>{{item.diseaseName}}</em></p>
						<div style="height: 33%;">
						<p style="display: flex; align-items: center;margin-bottom: 10px;"><em class="reg_acu w3" style="display: block;">正确率：</em></p>
						<el-progress v-bind:percentage=item.accuracy|numFilter :format="format" style="width: 80%;"></el-progress>
						</div>
						<p><br/><em class="reg_time w3">检测时间：</em>
						{{item.updateTime | formatTimer}}
						</p>
					</div>
				</el-col>
			</el-row>
		</li>
	</ul>
  </div>    
</template>

<script>
import WOW from 'wowjs';
  export default {
    name:'timeLocus',
    inject: ['reload'],
    data() {
      return {
        historyList:[],
        state:this.$store.state.timeLocus
      }
    },
	filters: {
		formatTimer: function(value) {
		  let date = new Date(value);
		  let y = date.getFullYear();
		  let MM = date.getMonth() + 1;
		  MM = MM < 10 ? "0" + MM : MM;
		  let d = date.getDate();
		  d = d < 10 ? "0" + d : d;
		  let h = date.getHours();
		  h = h < 10 ? "0" + h : h;
		  let m = date.getMinutes();
		  m = m < 10 ? "0" + m : m;
		  let s = date.getSeconds();
		  s = s < 10 ? "0" + s : s;
		  return y + "-" + MM + "-" + d + " " + h + ":" + m;
		},
		numFilter (value) {
			// 截取当前数据到小数点后两位
			let realVal = parseFloat(value).toFixed(2)
			return realVal
		}
	 },
    methods:{
        // 获取历史记录
		getHistoryList() {
			this.$http.get('/historyRecord/getHistory')
			.then(res => {
				if(res.data.code === 200) {
					console.log(res)
					this.historyList = res.data.data;
				}
			})
		}
    },
    created(){
	  this.getHistoryList();
    },
	mounted(){
	  let wow = new WOW.WOW({
	        boxClass: 'wow',
	        animateClass: 'animated',
	        offset: 0,
	        mobile: true,
	        live: true
	      });
	      wow.init();
	},
    activated(){
      if(this.$store.state.timeLocus == 1){
        // 为1说明当前文章数据被刷新了，需要重新刷新当前组件
        this.reload();
        this.$store.commit('changeTimeLocus',0)
      }
    }
  }
</script>

<style lang="scss" scoped>
.title {
  font-size: 16px;
  font-weight: 600;
}
.content {
  margin: 20px 0;
}
.describe {
	height: 100%;
	display: flex;
	flex-direction: column;
	justify-content: center;
}
.describe p{
	height: 33%;
	font-weight: bolder;
	font-size: 20px;
	width: 100%;
}
.w3 {
	margin-right: 30px;
}
</style>