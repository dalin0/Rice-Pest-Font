<template>
  <div>
    <div class="block recommend wow slideInRight">
      <title-box title="智能识别"></title-box>
      <div>
        <!-- 选择图片后展示 -->
        <img :src="f.src" width="350px" height="350px" v-if="f" />

        <!-- 选择图片之前展示 -->
        <img src="./upload.jpg" @click="add()" width="100px" height="100px" v-else />
        <input
            class="value"
            style="display:none"
            type="file"
            ref="file"
            accept="image/*"
            multiple="multiple"
            @change="getFile($event)"
        />
<!--        <img :src="loadImage" width="100px" height="100px" />-->
      </div>
      <el-button type="success" @click="submitForm($event)">虫害图片识别</el-button>
        <title-box class="assets" title="识别结果"></title-box>
      <div class="reg">
		  {{disease}}
      </div>
      <title-box class="assets" title="虫害详情信息"></title-box>
      <div class="reg">
		  {{content}}
      </div>
    </div>
  </div>
</template>

<script>
import WOW from 'wowjs';
import axios from "axios";
import Cookie from 'js-cookie'
import titleBox from '../components/TitleBox/titleBox.vue'
  export default {
    name:'recommend',
    components: { titleBox },
    props: {},
    data() {
      return {
        file: null,
        f: null,
        loadImage: "",
        isUploadSuccess: null,
		disease: '',
		content: ""
      }
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
    methods: {
      add() {
        this.$refs.file.click();
      },
      getFile(event) {
        this.file = event.target.files[0];
        const item = {
          name: this.file.name,
          size: this.file.size,
          file: this.file
        };
        this.html5Reader(this.file, item);
        this.f = item;
      },
      // 将图片文件转成BASE64格式
      html5Reader(file, item) {
        const reader = new FileReader();
        reader.onload = e => {
          this.$set(item, "src", e.target.result);
        };
        reader.readAsDataURL(file);
      },
      submitForm(event) {
		if(!Cookie.get('token')){
			this.$message({
			    message: '请登录后进行操作',
			    type: 'warning'
			  });
			  this.$router.push({name:'login'})
			  return
		}
        event.preventDefault();
        let formData = new FormData();
        formData.append("file", this.file);
        let config = {
          headers: {
            "Content-Type": "multipart/form-data"
          }
        };
        axios
            .post("http://localhost:8088/summer/img/upload", formData, config)
            .then(res => {
              console.log(res);
              if (res.status === 200) {
                // 收到传回结果
                this.disease = res.data.data.disease
                this.content = res.data.data.disOne.explanation
				this.loadImage = res.data.data.img_upload_url;
                this.$message({message:'识别成功',type:'success',offset: 80});
				// 收到结果之后发送请求让历史记录添加数据
				let data = {
					"disease_name":res.data.data.disease,
					"accuracy":res.data.data.probability,
					"pic_url":res.data.data.img_upload_url
				};
				this.$http.post('/historyRecord/insertHistory', data)
				.then(res => {
					console.log(res);
					// 上传成功，改变timeLocus状态
					this.$store.commit('changeTimeLocus', 1)
				})
              } else {
                this.$message({message:'识别失败',type:'error',offset: 80});
                console.log("失败" + 400);
              }
            });
      }
    }
  }
</script>

<style lang="scss" scoped>
 ul {
   margin-top: 10px;
   display: flex;
   flex-wrap: wrap;
   li {
      margin: 8px 9px;
      border-radius: 5px;
      padding: 10px 17px;
      border: 1px solid rgba(195, 194, 194, 0.5);
      cursor: pointer;
   }
   li:nth-child(n+1):hover {
     background:rgb(230, 54, 54);
     color: white;
   }
   li:nth-child(2n+2):hover {
     background:rgb(198, 230, 54);
     color: white;
   }
   li:nth-child(n+3):hover {
     background:rgb(54, 180, 230);
     color: white;
   }
   li:nth-child(2n+4):hover {
     background:rgb(230, 54, 163);
     color: white;
   }
 }
.reg {
	font-size: 18px;
	line-height: 3;
	margin-top: 3px;
}

 .assets {
   margin-top: 20px;
 }
 /deep/ .bottom {
	 background-image: linear-gradient(to right, red,orange,yellow,green,blue,indigo,violet);
	 border-bottom: 2px solid;
	 width: 15%;
 }
 /deep/ .title {
	color: darkgreen;
	font-size: 18px;
 }
</style>
