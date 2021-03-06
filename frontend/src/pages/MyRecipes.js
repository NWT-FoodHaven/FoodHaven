import React, { Component } from 'react';
import '../style/Home.css';
import Carousel from "react-multi-carousel";
import "react-multi-carousel/lib/styles.css";
import RecipeService from '../services/RecipeService';
import CategoryService from '../services/CategoryService';
import PictureService from '../services/PictureService';
import CategoryCard from '../components/CategoryCard';
import RecipeCard from '../components/RecipeCard';
import LoginLogout from '../components/LoginLogout';
import AuthService from '../services/AuthService';
import axios from 'axios';

const responsive = {
    superLargeDesktop: {
        breakpoint: { max: 4000, min: 3000 },
        items: 8
    },
    desktop: {
        breakpoint: { max: 3000, min: 1024 },
        items: 8
    },
    tablet: {
        breakpoint: { max: 1024, min: 464 },
        items: 4
    },
    mobile: {
        breakpoint: { max: 464, min: 0 },
        items: 2
    }
};

class MyRecipes extends Component {
    constructor(props) {
        super(props);
        this.recipeCategoryName = new Map();
        this.recipePhoto = new Map();
        this.state = {
            recipes: [],
            allrecipes: [],
            categories: [],
            pictures: [],
            categoryMap: new Map(),
            pictureMap: new Map(),
            categoryId: ''
        };
    }

    componentDidMount() {
        axios.get('http://localhost:8088/api/recipes/user?userId=' + AuthService.getCurrentUser().userId, {
            headers: {
                'Authorization': 'Bearer ' + AuthService.getCurrentUser().token,
                'Content-Type': 'application/json'
            }
        }).then((res) => {
            this.setState({ recipes: res.data });
            this.setState({ allrecipes: res.data });
        });
        CategoryService.getCategories().then((res) => {
            this.setState({ categories: res.data });
        });
        PictureService.getPictures().then((res) => {
            this.setState({ pictures: res.data });
        })
    }

    searchData = (e) => {
        let list = this.state.recipes;
        if (e != '' && e != null) {
            list = this.state.recipes.filter((item) => (item.name.toLowerCase().includes(e.toLowerCase())));
            this.setState({ recipes: list })
        } else {
            this.setState({ recipes: this.state.allrecipes })
        }
    }
    searchByCategory = (id) => {
        let list = []
        RecipeService.getRecipes().then((res) => {
            this.setState({
                list: res.data
            })
        })
        list = this.state.list.filter((item) => (item.recipeCategory.includes(id)));
        this.setState({ recipes: list })
    }

    render() {
        this.state.pictures.map(
            picture => (
                this.state.pictureMap.set(picture.id, picture.picByte)
            )
        )
        {
            this.state.categories.map(
                category => (
                    this.state.categoryMap.set(category.id, category.name)
                ))
        }
        return (
            <div>
                <div className='home-div'>
                    <h2 className='h2-style'>My recipes</h2>
                    <LoginLogout />
                </div>
                <div className="Search">
                    <span className="SearchSpan">
                        <i className="fas fa-search" />
                    </span>
                    <input
                        className="SearchInput"
                        type="text"
                        onChange={(e) => this.searchData(e.target.value)}
                        placeholder="Search recipes"
                    />
                </div>
                <div className="cards">
                    {this.state.recipes.map(
                        recipe => (
                            <RecipeCard key={recipe.id}
                                recipeId={recipe.id}
                                img={this.state.pictureMap.get(recipe.recipePicture)}
                                name={recipe.name}
                                category={this.state.categoryMap.get(recipe.recipeCategory)}
                                description={recipe.description}
                                rating={4.29}
                                currentUserId = {AuthService.getCurrentUser().userId}
                                userID = {recipe.userID}
                            />
                        )
                    )}
                </div>
            </div>
        );
    }
}

export default MyRecipes;
