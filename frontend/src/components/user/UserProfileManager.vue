<script setup>
import UserProfile from "@/components/user/UserProfile.vue";
import {onMounted, ref} from "vue";
import {getItemById} from "@/libs/fetchUtil.js";
import {decodeJWT} from "@/libs/decodeJWT.js";

const accessToken = sessionStorage.getItem('accessToken');
const getUser = decodeJWT(accessToken);


const user = ref({})

onMounted(async () => {
  try {
    const data = await getItemById(`${import.meta.env.VITE_APP_URL}/v2/users`, getUser.id)
    user.value = data
  } catch (error) {
    console.error("Error fetching user data:", error);
  }
})

</script>

<template>
  <UserProfile :user="user" />
</template>

<style scoped>

</style>