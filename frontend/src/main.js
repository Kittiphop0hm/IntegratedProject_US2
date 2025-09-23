import './assets/main.css'

// import { createApp } from 'vue'
// import { createPinia } from 'pinia'
// // import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
// import App from './App.vue'
// import router from './router'

// const app = createApp(App)
// const pinia = createPinia();
// app.use(router)
// app.use(pinia)
// // pinia.use(piniaPluginPersistedstate);


// app.mount('#app')
import { createApp } from "vue";
import App from "./App.vue";

import router from "./router";

import { createPinia } from "pinia";
createApp(App).use(router).use(createPinia()).mount("#app");