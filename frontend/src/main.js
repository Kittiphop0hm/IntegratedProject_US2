import "./assets/main.css";

import { createApp } from "vue";
import { createPinia } from "pinia";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
import App from "./App.vue";
import router from "./router";

const app = createApp(App);
const pinia = createPinia();

// ติด plugin ก่อน
pinia.use(piniaPluginPersistedstate);

app.use(pinia);
app.use(router);

app.mount("#app");

// app.mount('#app')
// import { createApp } from "vue";
// import App from "./App.vue";

// import router from "./router";

// import { createPinia } from "pinia";
// createApp(App).use(router).use(createPinia()).mount("#app");
