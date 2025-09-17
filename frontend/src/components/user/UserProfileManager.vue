<script setup>
import UserProfile from "@/components/user/UserProfile.vue";
import {onMounted, ref} from "vue";
import {addItem, editItem, getItemById} from "@/libs/fetchUtil.js";
import {decodeJWT} from "@/libs/decodeJWT.js";
import { useRoute, useRouter } from "vue-router";

const router = useRouter()
const accessToken = sessionStorage.getItem('accessToken')
const getUser = decodeJWT(accessToken)
const user = ref({})
const userPictureCard = ref([])

onMounted(async () => {
  try {
    const data = await getItemById(`${import.meta.env.VITE_APP_URL}/v2/users`, getUser.id)
    const pictures = await getItemById(`${import.meta.env.VITE_APP_URL}/v2/users/picture`, getUser.id)
    userPictureCard.value = pictures
    user.value = data
  } catch (error) {
    console.error("Error fetching user data:", error);
  }
})

const isSuccess = ref(false)
const isError = ref(false)
const editUser = async (currentUser) => {
  if (currentUser) {
    const updateUser = await editItem(`${import.meta.env.VITE_APP_URL}/v2/users`, currentUser.id, currentUser) 
    router.push({name: 'UserProfile'})
    if (updateUser.status === 200) {
        user.value = updateUser
        isSuccess.value = true
        setTimeout(() => {
          isSuccess.value = false
        }, 5000); 
    } else {
        isError.value = true
        setTimeout(() => {
          isError.value = false
        }, 5000);
    }
  }
}
</script>

<template>
  <UserProfile :user="user" :userPictureCard="userPictureCard" :isSuccess="isSuccess" :isError="isError" @updateUser="editUser"  />
</template>

<style scoped>

</style>