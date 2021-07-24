<template>
  <div>
    <div class="block">
      <!-- 标题 -->
      <title-box title="水稻百科"></title-box>
      <!-- 列表 -->
      <ul>
        <li :key="index" v-for="(item,index) in AllArticle" @click="GotoArticleDetail(item.id,item.clickNum)">
          <div class="title">{{item.diseaseName}}</div>
          <div class="content">
            <div class="image">
                <img v-lazy="item.picUrl" alt="">
            </div>
            <div class="describe">
              <p>{{item.diseaseExplanation}}</p>
              <div class="tag">
                <div v-for="(x,index) in item.classifyList" :key="index" style="display: inline-block">
                  <el-tag v-show="index==0">{{x.classifyName}}</el-tag>
                  <el-tag v-show="index==1" type="success">{{x.classifyName}}</el-tag>
                  <el-tag v-show="index==2" type="danger">{{x.classifyName}}</el-tag>
                </div>
              </div>
              <div class="describe-bottom">
                <span class="author">CodeMaster</span>
                <span v-if="item.createTime" class="timer">{{item.createTime.slice(0,10)}}</span>
                <span class="browse"><i class="el-icon-view"></i>{{viewNum}}</span>
                <span class="like"><i class="iconfont  My-new-icondianzan"></i>{{item.clickNum}}</span>
                <el-button size="mini" type="primary" plain>阅读文章</el-button>
              </div>
            </div>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import TitleBox from '../TitleBox/titleBox.vue'
  export default {
    props:['AllArticle'],
    components:{
      TitleBox
    },
    data(){
      return{
        viewNum : Math.round(Math.random()*1000)
      }
    },
    methods:{
           // 去文章详情
      GotoArticleDetail(id,clickNum){
        this.$router.push({name:'detail',params: {id:id,likeCount:clickNum}})
      }
    }
  }
</script>

<style lang="scss" scoped>
ul {
  margin-top: 15px;
}
li {
  margin:20px 0 30px 0;
  cursor:pointer;
}
.title {
  margin-bottom: 10px;
  font-size: 16px;
  font-weight: 600;
}
.content {
  display: flex;
  .image {
    overflow: hidden;
    border-radius: 5px;
    cursor:pointer;
    width: 200px;
    height: 130px;
    img {
      width: 100%;
      height: 100%;
      transition: all 0.4s;
    }
    img:hover {
      transform: scale(1.3);
    }
  }
  .describe {
    margin-left: 10px;
    flex:5;
    position: relative;
    overflow: hidden;
    height: 150px;
    .tag {
      margin: 5px 0;
      .el-tag {
        margin-right: 10px;
      }
    }

     p {
        display: block;
        font-size: 14px;
        color: #666;
        // 文字溢出隐藏
        overflow: hidden;
        display: -webkit-box;
        text-overflow: ellipsis;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 3;
        line-height: 26px;
      }
    .describe-bottom {
      position: absolute;
      bottom: 0;
      width: 100%;
      line-height: 28px;
      i {
        margin-right: 5px;
      }
      span {
        margin: 0 10px;
        font-size: 14px;
        color: rgb(107, 107, 107);
      }
      .author {
        margin-left: 0;
      }

      .el-button {
        float: right;
      }
    }
  }
}
</style>
