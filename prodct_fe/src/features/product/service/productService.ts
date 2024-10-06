import { CreateProductV1, ProductV1 } from "../type/productTypes";

const API_URL = "http://localhost:8080/api/v1/";
const PRODUCTS_PATH = "products";
const SEARCH_PATH = "search";

export const fetchProducts = async (): Promise<ProductV1[]> => {
  const response = await fetch(`${API_URL}/${PRODUCTS_PATH}`);
  return response.json();
};

export const fetchProductById = async (id: string): Promise<ProductV1> => {
  const response = await fetch(`${API_URL}/${PRODUCTS_PATH}/${id}`);
  return response.json();
};

export const fetchProductsFuzzySearch = async (query: string): Promise<ProductV1[]> => {
  const response = await fetch(`${API_URL}/${SEARCH_PATH}?qyery=${query}`);
  return response.json();
};

export const addProduct = async (product: CreateProductV1): Promise<ProductV1> => {
  const response = await fetch(`${API_URL}/${PRODUCTS_PATH}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(product),
  });
  return response.json();
};

export const updateProduct = async (product: ProductV1): Promise<ProductV1> => {
  const response = await fetch(`${API_URL}/${PRODUCTS_PATH}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(product),
  });
  return response.json();
};

export const deleteProduct = async (id: string): Promise<void> => {
  await fetch(`${API_URL}/${PRODUCTS_PATH}/${id}`, {
    method: "DELETE",
  });
};
