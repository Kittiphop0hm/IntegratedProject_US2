<script setup>
import { useRoute, useRouter } from 'vue-router';
import RegisterAndLogin from './RegisterAndLogin.vue';
import { onMounted, ref } from 'vue';

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
<RegisterAndLogin :roles="roles"></RegisterAndLogin>

</template>

<style scoped>

</style>