import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { ProductV1 } from "../type/productTypes";
import { fetchProductById } from "../service/productService";
import BackButton from "../../../component/BackButton";

const Product: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [fetchedProduct, setFetchedProduct] = useState<ProductV1 | null>(null);

  useEffect(() => {
    const loadProduct = async (id: string) => {
      if (id) {
        const product = await fetchProductById(id);
        setFetchedProduct(product);
      }
    };
    if (id) {
      loadProduct(id);
    }

  }, [id]);

  return (
    <div className="m-2">
      <BackButton />
      <h1>Product</h1>
      {fetchedProduct ? (
        <div>
          <h4>Id</h4>
          <p>{fetchedProduct.id}</p>
          <h4>Name</h4>
          <p>{fetchedProduct.name}</p>
          <h4>Category</h4>
          <p>{fetchedProduct.category}</p>
          <h4>Description</h4>
          <p>{fetchedProduct.description}</p>
          <h4>Price</h4>
          <p>{fetchedProduct.price}</p>
          <h4>Image URL</h4>
          <p>{fetchedProduct.imageUrl}</p>
        </div>
      ): <div>Loading...</div>}
    </div>
  );
};

export default Product;

