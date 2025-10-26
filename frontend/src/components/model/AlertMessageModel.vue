<script setup>
const props = defineProps({
    isSuccess: {
        type: Boolean ,
        default: true
    } ,
    isWarning: {
        type: Boolean ,
        default: false
    }
})
</script>
<template>
  <div >
    <div class="bg-white shadow-xl rounded-lg px-8 pt-6 pb-8">
        <h1 v-show="props.isSuccess === true" class="text-2xl font-semibold text-green-400">Successfully</h1>
        <h1 v-show="props.isSuccess === false && props.isWarning === false" class="text-2xl font-semibold text-red-400">Error</h1>
        <h1 v-show="props.isWarning === true && props.isSuccess === false" class="text-2xl font-semibold text-orange-400">Warning</h1>
        <br />
        <p class="itbms-message font-semibold"><slot name="message"></slot></p>
    </div>
  </div>
</template>
<style scoped></style>
