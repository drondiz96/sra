const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,

  devServer: {
    host: 'phonereview', // слушает на всех IP, включая 192.168.0.105
    port: 8081,
    allowedHosts: 'all', // разрешаем любые заголовки Host
    historyApiFallback: true // важно для SPA, чтобы не было 404 при перезагрузке
  }
})
