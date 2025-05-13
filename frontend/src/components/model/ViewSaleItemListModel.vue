<script setup>
const props = defineProps({
    items: {
        type: Array,
        required: true
    }
})
</script>

<template>
    <div>
        <div>
            <slot name="heading"></slot>
        </div>

        <div>
            <slot name="event"></slot>
        </div>

        <div>
            <ul>
                <li v-for="(item, index) in items" :key="index">
                    <slot name="listItems" :yourItems="item">Enter item list</slot>
                </li>
            </ul>
        </div>
    </div>
</template>