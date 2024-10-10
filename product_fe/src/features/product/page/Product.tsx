import React, { useEffect, useId, useState } from "react";
import { useParams } from "react-router-dom";
import { ProductV1 } from "../type/productTypes";
import { fetchProductById } from "../service/productService";
import BackButton from "../../../component/BackButton";

const Product: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [fetchedProduct, setFetchedProduct] = useState<ProductV1 | null>(null);
  const productId = useId();
  const nameId = useId();
  const categoryId = useId();
  const descriptionId = useId();
  const priceId = useId();
  const imageUrlId = useId();

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
          <img id={imageUrlId} src={fetchedProduct.imageUrl} alt="No image" />
          <h4>Id</h4>
          <p id={productId}>{fetchedProduct.id}</p>
          <h4>Name</h4>
          <p id={nameId}>{fetchedProduct.name}</p>
          <h4>Category</h4>
          <p id={categoryId}>{fetchedProduct.category}</p>
          <h4>Description</h4>
          <p id={descriptionId}>{fetchedProduct.description}</p>
          <h4>Price</h4>
          <p id={priceId}>€{fetchedProduct.price}</p>
        </div>
      ) : <div>Loading...</div>}
    </div>
  );
};

export default Product;

