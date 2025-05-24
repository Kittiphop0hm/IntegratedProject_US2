<script setup>
import { computed, ref, watch } from 'vue';

const emit = defineEmits(['massageValidate'])

const props = defineProps({
    model: {
        type: String
    }, 
    brand: {
        type: Object
    },
    description: {
        type: String
    },
    price: {
        type: Number
    }, 
    ram: {
        type: Number
    },
    screen: {
        type: Number
    },
    storage: {
        type: Number
    }, 
    color: {
        type: String
    },
    quantity: {
        type: Number
    },
    brandName: {
        type: String
    }, 
    brandUrl: {
        type: String
    },
    brandCountry: {
        type: String
    }
})
const brandNameErrorMassage = ref("")
const brandUrlErrorMassage = ref("")
const brandCountryErrorMassage = ref("")

watch(() => props.brandName, (brandName) => {
    if (brandName.length < 1 || brandName.length > 30) {
        brandNameErrorMassage.value = 'Brand name must be 1-30 characters long.'
        // console.log(brandNameErrorMassage.value);
        emit('massageValidate', brandNameErrorMassage.value, brandUrlErrorMassage.value, brandCountryErrorMassage.value)
        
    } else {
        brandNameErrorMassage.value = ''
        // console.log(brandNameErrorMassage.value);
        emit('massageValidate', brandNameErrorMassage.value, brandUrlErrorMassage.value, brandCountryErrorMassage.value)
    }
});

watch(() => props.brandUrl, (brandUrl) => {
    // console.log(brandUrl);
    if (!brandUrl.toLowerCase().includes('www')) {
        brandUrlErrorMassage.value = 'Brand URL must be a valid URL or not specified.'
        // console.log(brandUrlErrorMassage.value);
        emit('massageValidate', brandNameErrorMassage.value, brandUrlErrorMassage.value, brandCountryErrorMassage.value) 
    } else {
        brandUrlErrorMassage.value = ''
        // console.log(brandUrlErrorMassage.value);
        emit('massageValidate', brandNameErrorMassage.value, brandUrlErrorMassage.value, brandCountryErrorMassage.value)  
    }
})

watch(() => props.brandCountry, (brandCountry) => {
    // console.log(brandCountry);
    if (brandCountry.length < 1 || brandCountry.length > 80) {
        brandCountryErrorMassage.value = "Brand country of origin must be 1-80 characters long or not specified."
        // console.log(brandCountryErrorMassage.value);
        emit('massageValidate', brandNameErrorMassage.value, brandUrlErrorMassage.value, brandCountryErrorMassage.value)
    } else {
        brandCountryErrorMassage.value = ''
        emit('massageValidate', brandNameErrorMassage.value, brandUrlErrorMassage.value, brandCountryErrorMassage.value)
    }
})
</script>

<template>

</template>