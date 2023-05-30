function ProductCard(props) {

    const handleAddItemToCart = (e) => {
        props.loggedInUser.cart.push({
            id: props.item_id,
            item: props.title,
            price: props.price
        })
        props.setLoggedInUser({...props.loggedInUser});
    } 

    return (
        <div id="product-div">
            <div className="card" style={{width: "18rem"}}>
                <img src={props.img} className="card-img-top" alt="..." />
                <div className="card-body">
                    <h5 className="card-title">{props.title}</h5>
                    <p className="card-text">{props.desc}</p>
                    <p className="card-text">${props.price}</p>
                    <button className="btn btn-primary" style={{height: "65px"}} onClick={handleAddItemToCart}>Add to cart</button>
                </div>
            </div>
        </div>
    );
}
export default ProductCard;