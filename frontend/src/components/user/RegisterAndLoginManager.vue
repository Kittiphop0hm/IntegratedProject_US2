<script setup>
import { useRoute, useRouter } from "vue-router";
import RegisterAndLogin from "./RegisterAndLogin.vue";
import { onMounted, ref } from "vue";
import { register, addItem } from "@/libs/fetchUtil";


const router = useRouter();
const roles = ref([
  {
    text: "Buyer",
    value: "BUYER",
  },
  {
    text: "Seller",
    value: "SELLER",
  },
]);
const is401 = ref(false)
const is400 = ref(false)
const registerForm = async (event, user) => {
  event.preventDefault();
  console.log(user);
  console.log("Submit");
  console.log(user.files);
  try {
    const registerUser = await register(
      `${import.meta.env.VITE_APP_URL}/v2/users/register`,
      user,
      user.files
    );
    console.log(registerUser.status);
    console.log(registerUser.error);
    if (registerUser.status === 201) {
      router.push({ name: "SaleItemHome", query: { alertAddUser: "true" } });
    }
  } catch (err) {
    console.log(err);
  }
};
const loginUser = ref();
const loginForm = async (event, user) => {
  console.log(user);
  console.log("loginForm")
  event.preventDefault();
  try {
    loginUser.value = await addItem(
      `${import.meta.env.VITE_APP_URL}/v2/users/authentications`,
      user
    );
    console.log(loginUser.value.status);
    if (loginUser.value.status === 400) {
      is400.value = true
      is401.value = false
    }
    
    if(loginUser.value.status === 401){
      is401.value = true;
      is400.value = false
    }

  } catch (err) {
    console.log(err);
  }
};
</script>

<template>

  <RegisterAndLogin
    :roles="roles"
    :is401="is401"
    :is400="is400"
    @register="registerForm"
    @login="loginForm"
  ></RegisterAndLogin>
</template>

<style scoped></style>
