<script setup>
import { onMounted, ref, watchEffect } from 'vue';
import { RouterLink, RouterView } from 'vue-router'
import { decodeJWT } from './libs/decodeJWT';
import { addItemNoBody, addItem } from './libs/fetchUtil';

const isAccessTokenExpired = (accessToken) => {
  if (accessToken) {
    const decode = decodeJWT(accessToken)
    const currentTime = Math.floor(Date.now() / 1000)
    if (currentTime > decode.exp) return true
  }
  return false
}

const refreshAccessToken = async () => {
    const accessToken = localStorage.getItem("accessToken")
  if (accessToken && isAccessTokenExpired(accessToken)) {
    const newAccessToken = await addItemNoBody(`${import.meta.env.VITE_APP_URL}/v2/auth/refresh`)
    console.log(newAccessToken.data.access_token);
    localStorage.setItem('accessToken', newAccessToken.data.access_token);
    console.log(isAccessTokenExpired(accessToken));
  }
}
setInterval(async () => {
  console.log("1 minute")
}, 1 * 60 * 1000)

setInterval(async () => {
  console.log(" minute")
}, 1 * 60 * 3000)

// refreshAccessToken()

setInterval(async () => {
  refreshAccessToken()
  console.log("refresh token call new access token worked")
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
