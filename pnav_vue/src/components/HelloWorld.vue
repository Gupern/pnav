<template>
    <table id="table1" border="1" cellspacing="0">
        <!--cellspacing：边框为0-->
        <thead>
            <tr>
                <td colspan="2" align="center">常用书签</td>
            </tr>
        </thead>
        <tbody>
            <tr v-for="value in arr" :key="value.num">
                <td>
                    {{value.num}}
                </td>
                <td>
                    <a v-bind:href="value.link" target="_blank">
                        {{value.name}}
                    </a>
                </td>
            </tr>
        </tbody>
    </table>
</template>

<script>
    // 用export，不需要new Vue() {}
    export default {
        data () {
            return {
                arrs: [1, 2, 3, 4],
                arr: [
                    {
                        num: 1, name: '个人博客',
                        link: 'http://blog.gupern.com'
                    },
                    {
                        num: 2, name: '本项目Github地址',
                        link: 'https://github.com/Gupern/pnav'
                    },
                    {
                        num: 3, name: 'stackshare',
                        link: 'https://stackshare.io/Gupern/my-stack'
                    },
                ]
            }
        }
    }
</script>

<style>
#table1 {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  align-items: center;
  color: #2c3e50;
  /* margin-top: 60px; */
  margin: 0 auto;
}
</style>
