<script setup>
import { ref, watchEffect } from 'vue';
import { RouterLink, RouterView, useRouter } from 'vue-router'
import { decodeJWT } from './libs/decodeJWT';
import { addItemNoBody, addItem } from './libs/fetchUtil';
import { useUserStore } from './stores/users.js';

const router = useRouter();
const userStore = useUserStore();

const isAccessTokenExpired = (accessToken) => {
  if (accessToken) {
    try {
      const decode = decodeJWT(accessToken)
      const currentTime = Math.floor(Date.now() / 1000)
      if (currentTime > decode.exp) return true
    } catch (error) {
      return true
    }
  }
  return false
}

const refreshAccessToken = async () => {
  const accessToken = localStorage.getItem("accessToken")
  
  if (accessToken && isAccessTokenExpired(accessToken)) {
    try {
      const newAccessToken = await addItemNoBody(`${import.meta.env.VITE_APP_URL}/v2/auth/refresh`)
      console.log(newAccessToken.data.access_token);
      localStorage.setItem('accessToken', newAccessToken.data.access_token);
      console.log("✅ Access token refreshed successfully");
    } catch (error) {
      // ถ้า refresh token หมดอายุ (ได้ 401)
      console.error("❌ Refresh token expired:", error);
      
      // Remove stored tokens
      localStorage.removeItem('accessToken');
      localStorage.removeItem('pageSize');
      localStorage.removeItem('pageNumber');
      
      // Clear user store
      if (userStore && userStore.$reset) {
        userStore.$reset();
      }
      
      // Redirect to /signin
      console.log("🔄 Redirecting to signin...");
      router.push({ name: 'Login' });
    }
  }
}

setInterval(async () => {
  console.log("⏰ 1 minute passed")
}, 1 * 60 * 1000)

setInterval(async () => {
  console.log("⏰ 3 minutes passed")
}, 1 * 60 * 3000)

// Refresh token ทุก 2 นาที
setInterval(async () => {
  await refreshAccessToken()
  console.log("🔄 Refresh token check completed")
}, 1 * 60 * 2000)
</script>

<template>
  <RouterView />
</template>

<style>
@import url('https://fonts.googleapis.com/css2?family=Montserrat:ital,wght@0,100..900;1,100..900&display=swap');

body {
  font-family: "Montserrat", sans-serif;
}
</style>