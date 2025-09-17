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
const maskPhone = ref('')

onMounted(async () => {
  try {
    const data = await getItemById(`${import.meta.env.VITE_APP_URL}/v2/users`, getUser.id)
    user.value = data
    maskPhone.value = maskPhoneNumber(user.value.phoneNumber)
  } catch (error) {
    console.error("Error fetching user data:", error);
  }
})

const maskPhoneNumber = (number) => {
  if (!number) return '';

  const len = number.length;
  if (len < 4) {
    return 'x'.repeat(len);
  }

  const second = number.charAt(len - 2);
  const third  = number.charAt(len - 3);
  const fourth = number.charAt(len - 4);

  const head = 'x'.repeat(len - 4 > 0 ? len - 4 : 0);

  return `${head}${fourth}${third}${second}x`;
}

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
  <UserProfile 
  :user="user" 
  :maskPhoneNumber="maskPhone" 
  :isSuccess="isSuccess" 
  :isError="isError"
  @updateUser="editUser" />
</template>

<style scoped>

</style>