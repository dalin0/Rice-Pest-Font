<template>
  <div class="wrapper wow slideInLeft">
	<el-card class="card_body">
    <div class="content fl">
	
      <el-form :model="form" ref="form" :rules="rules" label-width="80px" :inline="false" size="normal">
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="form.nickname"></el-input>
        </el-form-item>
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            action="http://112.124.52.188:3000/api/article/upload"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
            name="head_img"
            >
            <img :src="form.avatar" class="avatar">
            <!-- <i v-else class="el-icon-plus avatar-uploader-icon"></i> -->
          </el-upload>
        </el-form-item>
		<el-form-item label="电话">
		  <el-input v-model="form.phone"></el-input>
		</el-form-item>
		<el-form-item label="邮箱">
		  <el-input v-model="form.email"></el-input>
		</el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">保存</el-button>
          <el-button @click="exit">退出登录</el-button>
        </el-form-item>
      </el-form>
      
    </div>
	<div class="content fr">
	  <el-form :model="pdform" ref="pdform" :rules="pdrules" label-width="80px" :inline="false" size="normal">
	    </el-form-item>
		<el-form-item label="新密码" prop="newpassword">
		  <el-input v-model="pdform.newpassword" autocomplete="off"></el-input>
		</el-form-item>
		<el-form-item label="确认密码" prop="checkpassword">
		  <el-input v-model="pdform.checkpassword" autocomplete="off"></el-input>
		</el-form-item>
	    <el-form-item>
	      <el-button type="primary" @click="submitPassword">修改密码</el-button>
	      <el-button @click="exit">退出登录</el-button>
	    </el-form-item>
	  </el-form>
	  
	</div>
	</el-card>
  </div>
</template>

<script>
import WOW from 'wowjs';
import Cookie from 'js-cookie'
export default {
    data() {
	// 验证密码逻辑
	  var validatePass = (rule, value, callback) => {
		  if(value === '') {
			  callback(new Error('请输入密码'))
		  }else {
			  if(value !== '') {
				  this.$refs.pdform.validateField('checkpassword')
			  }
			  callback()
		  }
	  };
	  var validatePass2 = (rule, value, callback) => {
		  if(value === '') {
			  callback(new Error('请输入密码'))
		  }else if(value !== this.pdform.newpassword) {
			  callback(new Error('两次输入的密码不一致'))
		  }else {
			  callback()
		  }
	  };
      return {
        form:{
          nickname:'',
		  avatar:'',
		  phone: '',
		  email:'',
		  id: 0,
		  isAdmin:0,
        },
		pdform: {
			newpassword:'',
			checkpassword:''
		},
        userInfo: {},
		rules: {
			nickname:[
				{ required: true, message: '昵称不能为空', trigger: 'blur' },
				{ min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur' }
			],
		},
		pdrules:{
			newpassword:[
				{ validator: validatePass, trigger: 'blur' }
			],
			checkpassword:[
				{ validator: validatePass2, trigger: 'blur' }
			]
		},
      }
    },
     methods: {
        handleAvatarSuccess(res) {
          this.form.avatar = res.data
        },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isJPG) {
		  this.$message({
			  message:'上传头像图片只能是 JPG 格式!',
			  type:'error',
			  offset: 80
		  })
        }
        if (!isLt2M) {
		  this.$message({
			  message:'上传头像图片大小不能超过 2MB!',
			  type:'error',
			  offset: 80
		  })
        }
        return isJPG && isLt2M;
      },
      exit(){
        // 清除Cookie
        Cookie.set('token','')
        // 清除sessionStorage
        sessionStorage.clear();
        this.$router.push({path:'/'})
        location.reload();
      },
      // 获取用户信息
      GetInfo(){
         this.$http.get('/user/getUserInfo').then( (res) => {
		   this.userInfo = res.data.data;
		   this.form.nickname = this.userInfo.nickname
		   this.form.avatar = this.userInfo.avatar
		   this.form.phone = this.userInfo.phone
		   this.form.email = this.userInfo.email
		   this.form.id = this.userInfo.id
		   this.form.isAdmin = this.userInfo.isAdmin
		   console.log(this.form)
         })
      },
      // 更新用户信息
      onSubmit(){
        // if(this.nickname === '怪蜀黍' && this.imageUrl === this.imageUrl){
        //   return this.$message.error('昵称已经被占用了，请换一个昵称吧~');
        // }
		this.$refs["form"].validate((valid) =>{
			if(valid) {
				this.$http.post('/user/updateUser', this.form)
				.then(res => {
					if(res.data.code == 200) {
						this.$message({message:res.data.msg, type:"success", offset:80})
						// 刷新页面
						location.reload();
					} else {
						this.$message({message:res.data.msg, type:"error", offset:80})
					}
				})
			} else {
				this.$message({message:"填写信息有误", type:"warning", offset:80})
			}
		})
      },
	  submitPassword() {
		  this.$refs["pdform"].validate((valid) => {
			 if(valid) {
				 this.$http.post('/user/updateUserPassword', this.pdform)
				 .then(res =>{
					 if(res.data.code == 200) {
						 this.$message({message:res.data.msg, type:"success", offset:80})
						 // 刷新页面
						 // 延迟1秒刷新
						 setTimeout(() => {
							 location.reload();
						 },1000)
					 } else {
						 this.$message({message:res.data.msg, type:"error", offset:80})
					 }
				 })
			 } else {
				 this.$message({message:"请按提示正确填写信息", type:"warning", offset:80})
			 }
		  });
	  }
    },
    created(){
      this.GetInfo();
      console.log(this.defaultAvatar);
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
.content {
  width: 40%;
//  margin: 5% auto;
  color: #fff!important;
}
.fl {
	float: left;
}
.fr {
	float: right;
}
.card_body {
	background-color: rgba(128,202,169,0.6);
	border: none;
	padding: 5%;
}
// 加上/deep/可以修改element-ui组件的样式
/deep/.el-card__body {
	padding: 0px;
}
/deep/.el-form-item__label {
  font-size: 14px;
  color: #fff!important;
}
// 头像
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  /deep/.avatar-uploader-icon:hover {
    border-color: #ffffff!important;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>