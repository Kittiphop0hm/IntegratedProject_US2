<script setup>
import { useRoute, useRouter } from 'vue-router';
import RegisterAndLogin from './RegisterAndLogin.vue';
import { onMounted, ref } from 'vue';
import { register } from '@/libs/fetchUtil';

const router = useRouter()
const roles = ref([
    {
    text: 'Buyer',
    value: 'BUYER'
    },
    {
    text: 'Seller',
    value: 'SELLER'
    },
])

const registerForm = async (event, user) => {
    event.preventDefault()
    console.log(user);
    console.log("Submit");
    console.log(user.files);
    try {
        const registerUser = await register(`${import.meta.env.VITE_APP_URL}/v2/register`, user, user.files)
        console.log(registerUser.status);
        console.log(registerUser.error);  
        if (registerUser.status === 201) {
            router.push({ name: "SaleItemHome", query: { alertAddUser: "true" } });   
        }
    } catch(err) {
        console.log(err);
    }
}

</script>

<template>
<RegisterAndLogin :roles="roles" @register="registerForm"></RegisterAndLogin>

</template>

<style scoped>

</style>