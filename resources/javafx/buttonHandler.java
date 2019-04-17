@FXML
private void buttonHandler(ActionEvent event) {

    Task myTask = new Task<Void>() {

        @Override
        public Void call() {

            //processing...
            System.out.println(event.getSource().toString() + " clicked");

            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                /**
                 * Update UI
                 */

                }
            });

            return null;
        }

        @Override
        protected void succeeded() {
            super.succeeded();

        }

        @Override
        protected void cancelled() {
            super.cancelled();

        }
    };
    new Thread(myTask).start();
}