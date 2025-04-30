import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import SaleItemDetail from '../views/SaleItemDetail.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/sale-items/:id', 
      name: 'SaleItemDetail',
      component: SaleItemDetail
    }
  ]
})

export default router

