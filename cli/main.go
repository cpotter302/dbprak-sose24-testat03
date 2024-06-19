package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"github.com/AlecAivazis/survey/v2"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"strings"
)

var (
	ApiUrl = os.Getenv("API_URL")

	Options = []ApiOption{
		{"getProduct", "GET", "/api/products/{id}", "id", true, &survey.Input{Message: "Enter product ID:"}, &Response{}, nil},
		{"getProducts", "GET", "/api/products?pattern={pattern}", "pattern", true, &survey.Input{Message: "Enter pattern:"}, &Response{}, nil},
		{"getCategoryTree", "GET", "/api/categories/getCategoryTree", "", false, &survey.Input{}, &Response{}, nil},
		{"getProductsByCategoryPath", "GET", "/api/categories/getProductsByCategoryPath?path={path}", "path", true, &survey.Input{Message: "Enter path from root (/):"}, &Response{}, nil},
		{"getTopProducts", "GET", "/api/products/by-rating?top={top}", "top", true, &survey.Input{Message: "Enter ranking:"}, &Response{}, nil},
		{"getSimilarCheaperProduct", "GET", "/api/products/{id}/similiar-cheaper", "id", true, &survey.Input{Message: "Enter product ID:"}, &Response{}, nil},
		{Name: "addNewReview", Method: "POST", Endpoint: "/api/reviews/add", Param: "", RequiresInput: true, Input: &survey.Input{Message: "Enter review details:"}, Response: &Review{}, Questions: []*survey.Question{
			{Name: "customerId", Prompt: &survey.Input{Message: "Enter customer ID:"}, Validate: survey.Required},
			{Name: "rating", Prompt: &survey.Input{Message: "Enter rating:"}, Validate: survey.Required},
			{Name: "description", Prompt: &survey.Input{Message: "Enter description:"}, Validate: survey.Required},
			{Name: "productId", Prompt: &survey.Input{Message: "Enter product ID:"}, Validate: survey.Required},
		}},
		{Name: "addNewCustomer", Method: "POST", Endpoint: "/api/customers", Param: "", RequiresInput: true, Input: &survey.Input{Message: "Enter customer details:"}, Response: &Customer{}, Questions: []*survey.Question{
			{Name: "customerId", Prompt: &survey.Input{Message: "Enter customer ID:"}, Validate: survey.Required},
		}},
		{"getTrolls", "GET", "/api/customers/trolls?ratingBelow={ratingBelow}", "ratingBelow", true, &survey.Input{Message: "Enter rating (below):"}, &Response{}, nil},
		{"getOffers", "GET", "/api/products/{id}/offers", "id", true, &survey.Input{Message: "Enter product ID:"}, &Response{}, nil},
	}
)

type ApiOption struct {
	Name          string
	Method        string
	Endpoint      string
	Param         string
	RequiresInput bool
	Input         *survey.Input
	Response      interface{}
	Questions     []*survey.Question
}

type Review struct {
	Customer    Customer `json:"customer" ,survey:"customerId"`
	Rating      string   `json:"rating" ,survey:"rating"`
	Description string   `json:"description" ,survey:"description"`
	Product     Product  `json:"product" ,survey:"productId"`
}

func (r *Review) WriteAnswer(name string, value interface{}) error {
	switch name {
	case "customerId":
		return r.Customer.WriteAnswer(name, value)
	case "rating":
		r.Rating = value.(string)
	case "description":
		r.Description = value.(string)
	case "productId":
		return r.Product.WriteAnswer(name, value)
	}
	return nil
}

type Customer struct {
	CustomerId string `json:"customerId" ,survey:"customerId"`
}

func (c *Customer) WriteAnswer(name string, value interface{}) error {
	c.CustomerId = value.(string)
	return nil
}

type Product struct {
	ProductId string `json:"productId" ,survey:"productId"`
}

func (p *Product) WriteAnswer(name string, value interface{}) error {
	p.ProductId = value.(string)
	return nil
}

type Response struct {
	value string
}

func main() {

	var choices []string

	for _, option := range Options {
		choices = append(choices, option.Name)
	}

	var choice string
	prompt := &survey.Select{
		Message: "Choose an option:",
		Options: choices,
	}
	err := survey.AskOne(prompt, &choice)
	if err != nil {
		log.Fatal(err)
	}

	for _, option := range Options {
		if option.Name == choice {
			executeApiOption(option)
			break
		}
	}
}

func executeApiOption(option ApiOption) {
	var params = make(map[string]string)

	if option.RequiresInput {
		if option.Method == "GET" {
			var value string
			err := survey.AskOne(option.Input, &value)
			if err != nil {
				panic(err)
			}
			params[option.Param] = value
		} else {
			err := survey.Ask(option.Questions, option.Response)
			if err != nil {
				panic(err)
			}
		}
	}

	var respBody string
	var err error

	switch option.Method {
	case "GET":
		respBody, err = makeGetRequest(option.Endpoint, params)
	case "POST":
		respBody, err = makePostRequest(option.Endpoint, option.Response)
	default:
		log.Fatalf("Unsupported HTTP method: %s", option.Method)
	}

	if err != nil {
		panic(err)
	}

	fmt.Println(prettyPrintJSON(respBody))
}

func makeGetRequest(endpoint string, params map[string]string) (string, error) {
	for key, value := range params {
		endpoint = replacePlaceholder(endpoint, key, value)
	}

	endpoint = fmt.Sprintf("%s%s", ApiUrl, endpoint)
	fmt.Println(endpoint)
	resp, err := http.Get(endpoint)
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", err
	}

	return string(body), nil
}

func makePostRequest(endpoint string, postData interface{}) (string, error) {
	jsonData, err := json.Marshal(postData)
	if err != nil {
		return "", err
	}

	endpoint = fmt.Sprintf("%s%s", ApiUrl, endpoint)
	fmt.Println(endpoint)
	resp, err := http.Post(endpoint, "application/json", bytes.NewBuffer(jsonData))
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", err
	}

	return string(body), nil
}

func replacePlaceholder(endpoint, placeholder, value string) string {
	return strings.Replace(endpoint, fmt.Sprintf("{%s}", placeholder), value, -1)
}

func prettyPrintJSON(jsonStr string) string {
	var prettyJSON bytes.Buffer
	err := json.Indent(&prettyJSON, []byte(jsonStr), "", "  ")
	if err != nil {
		return jsonStr
	}
	return prettyJSON.String()
}
