// import { useEffect } from 'react';
import '../App.css';
import ProductCard from './ProductCard';

function Home(props) {

    return (
        <div id='homeDiv'>
            <h1 id="home-header">{props.title}</h1>
            {props.loggedInUser
            ?
            <div id='cardDiv'>
                {props.products.map((product, i) => {
                    return <ProductCard key={i} item_id={product.id} img={product.img} title={product.item} desc={product.type} price={product.price} loggedInUser={props.loggedInUser} setLoggedInUser={props.setLoggedInUser}/>
                })}
            </div>
            :
            <p> </p>
            }
        </div>
        
    );
}
export default Home;