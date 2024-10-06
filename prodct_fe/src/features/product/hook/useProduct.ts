import { useEffect, useState } from "react";
import {
  addProduct,
  deleteProduct,
  fetchProductById,
  fetchProducts,
  fetchProductsFuzzySearch,
  updateProduct,
} from "../service/productService";
import { CreateProductV1, ProductV1 } from "../type/productTypes";

export const useTests = () => {
  const [products, setProducts] = useState<ProductV1[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  const loadProducts = async () => {
    const fetchedProducts = await fetchProducts();
    setProducts(fetchedProducts);
    setLoading(false);
  };

  useEffect(() => {
    loadProducts();
  }, []);

  const reloadProducts = async () => {
    await loadProducts();
  };

  const getProductById = async (id: string) => {
    const test = await fetchProductById(id);
    return test;
  };

  const getProductsByQyery = async (query: string) => {
    const products = await fetchProductsFuzzySearch(query);
    setProducts(products);
  };

  const createProduct = async (product: CreateProductV1) => {
    const newProduct = await addProduct(product);
    setProducts((prev) => [...prev, newProduct]);
  };

  const editProduct = async (product: ProductV1) => {
    const updatedProduct = await updateProduct(product);
    setProducts((prevProducts) =>
      prevProducts.map((prev) => (prev.id === product.id ? updatedProduct : prev)),
    );
  };

  const removeProduct = async (id: string) => {
    await deleteProduct(id);
    setProducts((prev) => prev.filter((product) => product.id !== id));
  };

  return {
    products,
    loading,
    getProductById,
    getProductsByQyery,
    createProduct,
    editProduct,
    removeProduct,
    reloadProducts
  };
};
