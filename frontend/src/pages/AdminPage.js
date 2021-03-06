import React, { Component } from "react";
import '../style/AdminPage.css';
import UserManager from "./UserManager";
import AuthService from "../services/AuthService";
import ReviewManager from "./ReviewManager";
import RecipeManager from "./RecipeManager";
import AddCategory from "../components/AddCategory";
import AddIngredient from "../components/AddIngredient";
import IngredientsList from "../components/IngredientsList";
import CategoryList from "../components/CategoryList";
class AdminPage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            component: ''
        };
    }

    createUI() {
        {
            if (this.state.component === 'UserManager') {
                return <UserManager />
            } else if (this.state.component === 'ReviewManager') {
                return <ReviewManager />
            } else if (this.state.component === 'RecipeManager') {
                return <RecipeManager />
            } else if (this.state.component === 'IngredientsList') {
                return <IngredientsList />
            } else if (this.state.component === 'CategoryList') {
                return <CategoryList />
            }
        }
    }

    setComponent = (name) => {
        this.setState({ component: name })
    }

    render() {
        return (
            <div>
                <div style={{ height: '1000px' }} className="column1">
                    <h2 className="h2-user-manager" onClick={event => {
                        event.preventDefault();
                        window.location.href = './ManageAccount';
                    }}><i className="fas fa-user-circle"></i></h2>
                    <h2 style={{ textAlign: "center" }}>Hello Admin</h2>
                    <div>
                        <button style={{ marginBottom: '10%' }} className='button-logout' onClick={AuthService.logout}><a
                            style={{ color: "white" }}>Log Out</a></button>
                        <button onClick={() => this.setComponent('UserManager')} className='h3-admin'><i
                            className="fa fa-user-group"></i> Manage users
                        </button>
                        <br />
                        <button onClick={() => this.setComponent('ReviewManager')} className='h3-admin'><i
                            className="fas fa-star"></i> Manage reviews
                        </button>
                        <br />
                        <button onClick={() => this.setComponent('RecipeManager')} className='h3-admin'><i
                            className="fa fa-book"></i> Manage recipes
                        </button>
                        <br />
                        <button onClick={() => this.setComponent('CategoryList')} className='h3-admin'>
                            <i className="fas fa-concierge-bell"></i> Category list
                        </button>
                        <br />
                        <button onClick={() => this.setComponent('IngredientsList')} className='h3-admin'>
                            <i className="fas fa-carrot"></i> Ingredients list
                        </button>
                        <br />
                    </div>
                </div>
                <div className="column2">
                    <h2 style={{ marginLeft: "40%" }} className='h2-style' onClick={event => {
                        event.preventDefault();
                        window.location.href = './Home';
                    }}>FoodHaven</h2>
                    {this.createUI()}
                </div>
            </div>
        );
    }
}

export default AdminPage;
