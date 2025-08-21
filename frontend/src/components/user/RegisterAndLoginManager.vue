<script setup>
import { useRoute, useRouter } from 'vue-router';
import RegisterAndLogin from './RegisterAndLogin.vue';
import { onMounted, ref } from 'vue';

const route = useRoute()
const pathName = ref(route.name)

const roles = ref([
    {
    text: 'Seller',
    value: 'seller'
    },
    {
    text: 'Buyer',
    value: 'buyer'
    },
])

</script>

<template>
<RegisterAndLogin :roles="roles" :pathName="pathName"></RegisterAndLogin>

</template>

<style scoped>

</style>