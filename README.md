# ML-QuadratPP (ML2++)

**ML2++** is a model-driven framework and domain-specific modeling language (DSL) for building **IoT + Machine Learning** applications with a strong focus on **time-series forecasting** and automated ML workflows.

With ML2++, users define:
- **IoT system structure** (things/components, messages, ports, deployment aspects)
- **Data analytics configuration** (input/output features, preprocessing steps, ML/forecasting model settings)
- **Training and evaluation settings** (splits, metrics, plots, and reporting)

ML2++ aims to reduce manual coding by generating the key artifacts needed to run an ML-enabled IoT pipeline, including:
- preprocessing configuration and scripts
- model training and prediction workflow (e.g., forecasting for the next *k* steps)
- evaluation outputs (metrics and visualizations)
- example datasets and use-case models

## Main components

- **Textual Editor (Xtext / Eclipse / Maven):** write and validate ML2++ models as text.
- **Graphical Editor (Sirius Web + Docker):** create ML2++ models visually in a web environment.
- **Examples:** sample models and small datasets for quick experimentation.
- **Documentation:** tutorials and paper PDFs describing the language and tool.

> If you use this repository in academic work, please cite the corresponding papers in `docs/papers/`.
