const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,

  devServer: {
    host: 'phonereview',
    port: 8081,
    allowedHosts: 'all',

    setupMiddlewares(middlewares, devServer) {
      // отключаем проверку Host-заголовка
      devServer.app.use((req, res, next) => {
        req.headers.host = 'localhost'; // подменяем заголовок
        next();
      });
      return middlewares;
    }
  }
});
