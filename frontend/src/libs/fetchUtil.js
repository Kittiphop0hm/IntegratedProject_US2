async function getItems(url) {
    try {
      const data = await fetch(url)
      const items = await data.json()
      return items
    } catch (error) {
      throw new Error('can not get your items')
    }
  }
  async function getItemById(url, id) {
    try {
      const data = await fetch(`${url}/${id}`)
      const item = await data.json()
      return item
    } catch (error) {
      if (data.status === 404) return undefined
      throw new Error('can not get your item')
    }
  }
  
  async function deleteItemById(url, id) {
    try {
      const res = await fetch(`${url}/${id}`, {
        method: 'DELETE'
      })
      return res.status
    } catch (error) {
      throw new Error('can not delete your item')
    }
  }
  
  async function addItem(url, newItem) {
    try {
      const res = await fetch(url, {
        method: 'POST',
        headers: {
          'content-type': 'application/json'
        },
        body: JSON.stringify({
          ...newItem
        })
      })
      const addedItem = await res.json()
      return {
        status: res.status,
        data: addedItem
      }
    } catch (error) {
      throw new Error('can not add your item')
    }
  }
  
  async function editItem(url, id, editItem) {
    try {
      const res = await fetch(`${url}/${id}`, {
        method: 'PUT',
        headers: {
          'content-type': 'application/json'
        },
        body: JSON.stringify({
          ...editItem
        })
      })
      const editedItem = await res.json()
      return {
        ...editedItem,
        status: res.status
      }
    } catch (error) {
      throw new Error('can not edit your item')
    }
  }

  async function addImage(url, file, saleId) {
    try {
      const formdata = new FormData()
      formdata.append("file", file)
      formdata.append("saleId", saleId)
      const res = await fetch(`${url}`, {
        method: "POST",
        body: formdata
      })
      const data = await res.text()
      return {
        data: data,
        status: res.status
      }
    } catch(err) {
      console.log(err);
    }
  }
  export { getItems, getItemById, deleteItemById, addItem, editItem, addImage }