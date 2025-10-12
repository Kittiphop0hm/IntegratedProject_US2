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
    <div class="bg-black/5 shadow-xl rounded px-8 pt-6 pb-8">
        <h1 v-show="props.isSuccess === true" class="text-2xl text-green-400">Successfully</h1>
        <h1 v-show="props.isSuccess === false && props.isWarning === false" class="text-2xl text-red-400">Error</h1>
        <h1 v-show="props.isWarning === true && props.isSuccess === false" class="text-2xl text-orange-400">Warning</h1>
        <br />
        <p class="itbms-message"><slot name="message"></slot></p>
    </div>
  </div>
</template>
<style scoped></style>
