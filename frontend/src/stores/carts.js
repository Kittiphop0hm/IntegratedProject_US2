import { find, forEach } from "lodash";
import { defineStore } from "pinia";
import { ref, computed } from "vue";

export const useCartStore = defineStore(
  "cart",
  () => {
    // reactive state
    const cartQuantity = ref(0);
    // const objForm = ref({
    //   sellerName: "",
    //   items: [
    //     {
    //       brandName: "",
    //       model: "",
    //       storageGb: 0,
    //       color: "",
    //       quantity: 0,
    //       price: 0,
    //     },
    //   ],
    // });
    const cartExample = [
      {
        sellerName: "Somsuk Fundee",
        items: [
          {
            brandName: "Apple",
            model: "iPhone 14",
            storageGb: 256,
            color: "Midnight",
            quantity: 1,
            price: 29700,
          },
          {
            brandName: "Apple",
            model: "iPhone 13",
            storageGb: 128,
            color: "Starlight",
            quantity: 2,
            price: 23900,
          },
        ],
      },
      {
        sellerName: "Nattapong Tech",
        items: [
          {
            brandName: "Samsung",
            model: "Galaxy S23",
            storageGb: 256,
            color: "Phantom Black",
            quantity: 1,
            price: 26900,
          },
          {
            brandName: "Samsung",
            model: "Galaxy S22",
            storageGb: 128,
            color: "White",
            quantity: 3,
            price: 19900,
          },
        ],
      },
    ];

    const cartObj = ref([]);
    // getters (computed)

    // actions
    function changeFormatObject(item) {
      return {
        sellerId: item.seller.id ,
        sellerName: item.seller.fullName.split(" ")[0],
        checked: false,
        items: [
          {
            description: item.description ,
            qtyInstock: item.quantity,
            saleItemId: item.id ,
            brandName: item.brandName,
            model: item.model,
            storageGb: item.storageGb,
            color: item.color,
            quantity: item.quantityEach,
            price: item.price,
            checked: false,
          },
        ],
      };
    }

    function findIndexSellerCartObj(obj2) {
      return cartObj.value.findIndex((obj1) => obj1.sellerName === obj2);
    }

    function findIndexItemCartObj(indexFoundSeller, obj2) {
      let copyObj2 = JSON.parse(JSON.stringify(obj2));
      delete copyObj2.quantity;
      delete copyObj2.qtyInstock;
      // let cartObjClone = {...cartObj.value}
      let cartObjClone = JSON.parse(JSON.stringify(cartObj.value));
      return cartObjClone[indexFoundSeller].items.findIndex((item) => {
        delete item.quantity;
        delete item.qtyInstock
        return JSON.stringify(item) === JSON.stringify(copyObj2);
      });
    }

    function isMaxQtyInStock(item, qty = 1 , from = "") {
      console.log(item.quantity);
      console.log(item);
      let newObjFormat ;
      if(from === ""){
        newObjFormat = changeFormatObject(item);
      } else {
        newObjFormat = item
      }
      console.log("------------ isMaxQtyInStock carts.js 83------------");
      console.log(newObjFormat);
      let qtyInstock;
      if(from===""){
        qtyInstock = item.quantity;
      } else{
        qtyInstock = item.qtyInstock;
      }
      console.log("qtyInstock:", qtyInstock);
      const indexSellerCartObj = findIndexSellerCartObj(
        // item.seller.fullName.split(" ")[0]
        newObjFormat.sellerName
      );
      console.log("indexSellerCartObj:", indexSellerCartObj);
      if (indexSellerCartObj === -1) {
        if (qty <= qtyInstock) {
          return false;
        }
        return "quantity in stock is max at " + qtyInstock;
      }
      console.log("indexSellerCartObj:", indexSellerCartObj);
      console.log("newObjFormat.items[0]:", newObjFormat.items[0]);
      const indexFoundItem = findIndexItemCartObj(
        indexSellerCartObj,
        newObjFormat.items[0]
      );
      console.log("indexFoundItem:", indexFoundItem);
      console.log("cartObj.value:", cartObj.value);
      if (indexFoundItem === -1) {
        return false;
      }
      const qtyInCart =
        cartObj.value[indexSellerCartObj].items[indexFoundItem].quantity;
      console.log("qtyInCart:", qtyInCart);
      console.log(
        "qtyInCart: " +
          qtyInCart +
          " + qty: " +
          qty +
          "= " +
          (qtyInCart + qty) +
          " <= qtyInstock: " +
          qtyInstock
      );
      if (qtyInCart + qty <= qtyInstock) {
        return false;
      } else {
        return "quantity in stock is max at " + qtyInstock;
      }
    }

    function increaseCartQuantity(qty = 1) {
      cartQuantity.value += qty;
    }

    function decreaseCartQuantity(qty = 1) {
      cartQuantity.value -= qty;
    }

    function pushInCart(item, qty = 1) {
      // const objCartForm = {
      //   sellerName: item.seller.fullName.split(" ")[0],
      //   items: [
      //     {
      //       brandName: item.brandName,
      //       model: item.model,
      //       storageGb: item.storageGb,
      //       color: item.color,
      //       quantity: item.quantityEach,
      //       price: item.price,
      //     },
      //   ],
      // };
      const objCartForm = changeFormatObject(item);
      if (cartObj.value.length === 0) {
        cartObj.value.push(objCartForm);
        increaseCartQuantity(qty);
        // console.log(cartObj.value);
      } else {
        const indexFoundSeller = findIndexSellerCartObj(objCartForm.sellerName);
        // const indexFoundSeller = cartObj.value.findIndex(
        //   (obj) => obj.sellerName === objCartForm.sellerName
        // );
        console.log("isFoundSeller");
        console.log(indexFoundSeller);
        if (indexFoundSeller === -1) {
          cartObj.value.push(objCartForm);
          increaseCartQuantity(qty);
        } else {
          console.log("Found Seller");
          const indexFoundItem = findIndexItemCartObj(
            indexFoundSeller,
            objCartForm.items[0]
          );
          // let indexFoundItem = -1;
          // const indexFoundItem = cartObj.value[indexFoundSeller].items.findIndex((item) => {
          //       return (
          //       JSON.stringify(item) === JSON.stringify(objCartForm.items[0])
          //     );
          // })
          // const isFoundItem = cartObj.value.some((obj) => {
          //   const indexItem = obj.items.findIndex((item) => {
          //     return (
          //       JSON.stringify(item) === JSON.stringify(objCartForm.items[0])
          //     );
          //   });
          //   if (indexItem > -1) {
          //     indexFoundItem = indexItem;
          //     return true;
          //   }
          //   return false;
          // });
          console.log("indexFoundItem");
          console.log(indexFoundItem);
          if (indexFoundItem !== -1) {
            console.log("isFoundItem !== -1");
            console.log(indexFoundItem);
            console.log(
              cartObj.value[indexFoundSeller].items[indexFoundItem].quantity
            );
            console.log(
              cartObj.value[indexFoundSeller].items[indexFoundItem].quantity
            );
            cartObj.value[indexFoundSeller].items[indexFoundItem].quantity +=
              qty;
            console.log(
              cartObj.value[indexFoundSeller].items[indexFoundItem].quantity
            );
            increaseCartQuantity(qty);
          } else {
            console.log("Not Found Items");
            console.log(objCartForm.items[0]);
            cartObj.value[indexFoundSeller].items.push(objCartForm.items[0]);
            increaseCartQuantity(qty);
          }
        }
        // for (const element of cartExample) {
        //   console.log(element);
        // }

        // for (const element of cartObj.value) {
        //   console.log(element.sellerName)
        //   if (element.sellerName !== objCartForm.sellerName) {
        //     cartObj.value.push(objCartForm);
        //   }
        //   else {
        //     if (
        //       JSON.stringify(cartObj.value.items) ===
        //       JSON.stringify(objCartForm.items)
        //     ) {
        //       objCartForm.items.quantity += 1
        //     }
        //     else {
        //       cartObj.value.items.push(objCartForm.items[0]);
        //     }
        //   }

        // }
      }
      console.log(cartObj.value);
    }

    function clearCart() {
      cartQuantity.value = 0;
      cartObj.value = [];
    }
    

    return {
      cartQuantity,
      increaseCartQuantity,
      decreaseCartQuantity,
      pushInCart,
      cartObj,
      isMaxQtyInStock,
      clearCart
    };
  },
  {
    persist: {
      storage: localStorage,
      paths: [""],
    },
  }
);
