<template>
<div>
    <div class="block wow slideInLeft">
      <!-- 您的位置 -->
      <location Tit1="首页" Tit2="水稻百科" class="wow slideInLeft"></location>
      <!-- 具体文章 -->
      <article-contents :data="data" class="wow slideInLeft"></article-contents>
      <!-- 点赞/打赏 -->
      <div class="likeBox">
          <el-button type="primary" v-bind:disabled=isClick round @click="like()"><i class="iconfont  My-new-icondianzan"></i>点赞（{{likeCount}}）</el-button>
          <el-button type="success" round @click="dialogVisible = true"><i class="iconfont My-new-icondashang"></i>打赏</el-button>
      </div>
      <!-- 弹框 -->
      <el-dialog
        :visible.sync="dialogVisible"
        width="30%"
        :before-close="handleClose"
        custom-class="tankuang"
        >
      </el-dialog>

      <!-- 评论 -->
      <comment :articleId="id"></comment>
    </div>
   </div>
</template>

<script>
import WOW from 'wowjs';
import comment from '@/components/Comment.vue'
import location from '../components/Location/location.vue'
import ArticleContents from '../components/ArticleContents/ArticleContents.vue'
import Cookie from 'js-cookie'
  export default {
    components:{
      comment,
      location,
      ArticleContents,

    },
    data() {
      return {
        id:this.$route.params.id,
        data: null,

        likeCount: this.$route.params.likeCount,

        isClick:false,

        dialogVisible: false
      }
    },
    methods:{
      GetArticleDetail(){
        this.$http.get('/disease/findDiseaseById',{
          params:{
            diseaseId:this.id
          }
        }).then(res => {
          console.log(res);
          if(res.data.code === 200 ){
            this.data = res.data.data;
          }
        })
      },
	  // 初始化点赞数量
	  getLinkCount() {
		  this.$http.get('/disease/findDiseaseById',{
			  params:{
				  diseaseId: this.id
			  }
		  }).then(res => {
			  if(res.data.code === 200 ) {
				  this.likeCount = res.data.data.clickNum
			  }
		  })
	  },
      // 点赞
      like(){
         if(Cookie.get('token')){
           this.$http.get('/disease/clickDisease',{
             params:{
               diseaseId:this.id
             }
          }).then(res => {
            this.likeCount = res.data.data
            this.isClick = true
          })
        }else {
          this.$message({
            message: '请登录后进行操作',
            type: 'warning'
          });
          this.$router.push({name:'login'})
        }
      }
    },
    created(){
      this.GetArticleDetail()
	  this.getLinkCount()
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
    }
  }
</script>

<style lang="scss" scoped>
.head {
   margin-top: 20px;
}
.likeBox {
  display: flex;
  justify-content: center;
  margin: 25px 0;
  .el-button {
    margin: 0 10px;
  }
  i {
    margin-right: 8px;
  }
}

.wx {
  width: 150px;
}
</style>
