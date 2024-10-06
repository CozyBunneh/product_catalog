export interface ProductV1 {
  id: string;
  name: string;
  category: string;
  description: string;
  price: number;
  imageUrl: string;
}

export interface CreateProductV1 {
  name: string;
  category: string;
  description: string;
  price: number;
  imageUrl: string;
}
