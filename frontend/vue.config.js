const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,

  devServer: {
    host: 'reviewphoneserve',
    port: 8081,
    allowedHosts: 'all'
  }
});
