<script setup>
import UserProfile from "@/components/user/UserProfile.vue";
import {onMounted, ref} from "vue";
import {getItemById} from "@/libs/fetchUtil.js";

// const getUser = JSON.parse(localStorage.getItem("currentUser"))
// const user = ref({})
//
// onMounted(async () => {
//   user.value = await getItemById(`${import.meta.env.VITE_APP_URL}`, getUser.id)
// })

</script>

<template>
  <UserProfile />
</template>

<style scoped>

</style>