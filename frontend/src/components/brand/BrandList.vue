<script setup>
import ViewBrandListModel from "../model/ViewBrandListModel.vue";
import ListTableModel from "../model/ListTableModel.vue";
import { ref } from "vue";
const props = defineProps({
  brands: {
    type: Array,
    required: true,
  },
});
</script>

<template>
  <div class="px-10">
    <ListTableModel :items="brands">
      <template #listItem="{ yourItem }">
        <td class="border px-2 py-1 itbms-id">{{ yourItem.id ?? "-" }}</td>
        <td class="border px-2 py-1 itbms-brand">
          {{ yourItem.name ?? "-" }}
        </td>
      </template>
      <template #action="{ yourItem }">
        <router-link :to="{ name: 'BrandEdit', params: { id: yourItem.id } }">
          <button class="btn btn-info mr-2">Edit</button>
        </router-link>

        <!--  -->
        <router-link to=/>
          <button class="btn btn-error ml-2">Delete</button>
        </router-link>
      </template>
    </ListTableModel>
  </div>
  <!-- <div class="px-8 py-5">
        <ViewBrandListModel :brands="brands">
            <template #listBrand="{ brands }"></template>
        </ViewBrandListModel>
    </div> -->
</template>
