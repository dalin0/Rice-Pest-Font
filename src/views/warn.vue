<template>
<div class="ibody">
  <main>
    <img src="../assets/img/warn.png">
    <section>警告!  访问此网站可能会损坏您的计算机！</section>
    <p>访问此页面可能遭受损害，此页面被报告为分发恶意软件。强烈建议您不要访问此网页。</p>
    <div id="frozen-btn">
      <button class="btn" @click="goBack">停止访问</button><a href="https://cn.bing.com/images/search?q=%E7%BE%8E%E4%B8%BD%E7%9A%84%E6%B0%B4%E7%A8%BB" style="text-decoration:underline; margin-left: 10px">忽略警告，恢复访问</a>
    </div>
  </main>
</div>
</template>

<script>
export default {
  name: "warn",
  methods:{
    goBack() {
      this.$router.replace({path:'/'})
    }
  }
}
</script>

<style lang="scss" scoped>
main {
  position: fixed;
  top: 15%;
  left: 50%;
  transform: translateX(-50%);
  color: white;
  section {
    font-size: 50px;
    margin: 10px;
  }
  #frozen-btn {
    margin-top: 10px;
    width: 100%;
    .btn{
      width: 200px;
      background: rgba(255,255,255,0.3);
    }

  }
}
.ibody{
  position: fixed;
  background: #bd2c00;
  width: 100%;
  height: 100%;
}

</style>