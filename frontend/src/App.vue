<script setup>
import { ref, watchEffect } from 'vue';
import { RouterLink, RouterView } from 'vue-router'
import { decodeJWT } from './libs/decodeJWT';
import { addItemNoBody, addItem } from './libs/fetchUtil';

const isAccessTokenExpired = (accessToken) => {
  if (accessToken) {
    const decode = decodeJWT(accessToken)
    const currentTime = ref(Math.floor(Date.now() / 1000))
    if (currentTime > decode.exp) return true
  }
  return false
}

setInterval(async () => {
  const accessToken = ref(sessionStorage.getItem("accessToken"))
  if (accessToken && isAccessTokenExpired(accessToken.value)) {
    const newAccessToken = await addItemNoBody(`${import.meta.env.VITE_APP_URL}/v2/auth/refresh`)
    console.log(newAccessToken.data.access_token);
    sessionStorage.setItem('accessToken', newAccessToken.data.access_token);
    console.log(isAccessTokenExpired(accessToken.value));
  }
}, 1 * 60 * 1000)
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